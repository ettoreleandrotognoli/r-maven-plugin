package io.github.ettoreleandrotognoli.r;

import io.github.ettoreleandrotognoli.r.domain.resource.ResourceDirectory;
import io.github.ettoreleandrotognoli.r.domain.resource.ResourceFile;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResourceNodeFactory {

    private static final class InstanceHolder {
        public static final ResourceNodeFactory INSTANCE = new ResourceNodeFactory();

    }

    public static ResourceNodeFactory getInstance() {
        return InstanceHolder.INSTANCE;
    }

    protected ResourceNodeFactory() {
    }

    String getParent(String path) {
        int index = path.lastIndexOf(File.separator);
        if (index != -1) {
            return path.substring(0, index);
        }
        return "";
    }

    public ResourceDirectory make(DirectoryScanner directoryScanner) {
        Map<String, ResourceDirectory> directoryNodeMap = new HashMap<>();

        for (String directory : directoryScanner.getIncludedDirectories()) {
            directoryNodeMap.put(directory, new ResourceDirectory(directory, null, new ArrayList<>()));
        }

        ResourceDirectory root = directoryNodeMap.remove("");

        for (ResourceDirectory resourceNode : directoryNodeMap.values()) {
            String path = resourceNode.getPath();
            String parentPath = getParent(path);
            ResourceDirectory parent = directoryNodeMap.getOrDefault(parentPath,root);
            resourceNode.setParent(parent);
            parent.getChildren().add(resourceNode);
        }

        for(String filePath : directoryScanner.getIncludedFiles()){
            String parentPath = getParent(filePath);
            ResourceDirectory parentNode = directoryNodeMap.getOrDefault(parentPath, root);
            ResourceFile fileNode = new ResourceFile(filePath, parentNode);
            parentNode.getChildren().add(fileNode);
        }
        return root;
    }

}
