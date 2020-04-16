package io.github.ettoreleandrotognoli.r.domain.resource;

import java.util.Collections;
import java.util.List;

public class ResourceFile implements ResourceNode{

    String path;
    ResourceDirectory parent;

    public ResourceFile(String path, ResourceDirectory parent) {
        this.path = path;
        this.parent = parent;
    }

    public ResourceFile() {
    }

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public ResourceDirectory getParent() {
        return parent;
    }

    public void setParent(ResourceDirectory parent) {
        this.parent = parent;
    }

    @Override
    public List<ResourceNode> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public void visit(ResourceNodeProcessor resourceNodeProcessor) {
        resourceNodeProcessor.process(this);
    }
}
