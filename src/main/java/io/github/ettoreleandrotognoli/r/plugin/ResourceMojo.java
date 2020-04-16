package io.github.ettoreleandrotognoli.r.plugin;

import com.squareup.javapoet.JavaFile;
import io.github.ettoreleandrotognoli.r.RClassBuilder;
import io.github.ettoreleandrotognoli.r.ResourceNodeFactory;
import io.github.ettoreleandrotognoli.r.domain.resource.ResourceNode;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.lang.String.format;

@Mojo(name = "generate-r", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class ResourceMojo extends AbstractMojo {


    @Parameter(defaultValue = "${project.build.directory}/generated-sources/r", required = true, readonly = true)
    File outputJavaDirectory;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;


    public ResourceNode parseResource(Resource resource) {
        DirectoryScanner directoryScanner = buildScanner("", resource);
        directoryScanner.scan();
        ResourceNodeFactory factory = ResourceNodeFactory.getInstance();
        return factory.make(directoryScanner);
    }

    private static DirectoryScanner buildScanner(String baseDir, Resource resource) {
        if (resource == null) {
            throw new IllegalArgumentException();
        }
        File fileBaseDir = new File(baseDir, resource.getDirectory());
        if (!fileBaseDir.exists() || !fileBaseDir.isDirectory() || !fileBaseDir.canRead()) {
            throw new IllegalArgumentException(format("Invalid <directory> %s in resource. Check your pom.xml", resource.getDirectory()));
        }

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(fileBaseDir.getPath());
        scanner.setIncludes(resource.getIncludes().isEmpty() ? null : resource.getIncludes().toArray(new String[0]));
        scanner.setExcludes(resource.getExcludes().toArray(new String[0]));
        return scanner;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException, MojoFailureException {
        project.getCompileSourceRoots().add(this.outputJavaDirectory.getAbsolutePath());
        Log log = getLog();

        RClassBuilder rClassBuilder = new RClassBuilder();

        List<?> resources = project.getResources();
        resources.stream()
                .filter(it -> it instanceof Resource)
                .map(it -> (Resource) it)
                .map(this::parseResource)
                .forEach(rClassBuilder::processTree);


        String packageName = project.getGroupId() + "." + project.getArtifactId().replaceAll("[\\.-]", "");
        JavaFile javaFile = JavaFile.builder(packageName, rClassBuilder.build())
                .build();

        try {
            javaFile.writeTo(outputJavaDirectory);
        } catch (IOException e) {
            log.error(e);
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
