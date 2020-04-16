package io.github.ettoreleandrotognoli.r.domain.resource;

import java.util.List;

public class ResourceDirectory implements ResourceNode {

    String path;
    ResourceDirectory parent;
    List<ResourceNode> children;

    public ResourceDirectory(String path, ResourceDirectory parent, List<ResourceNode> children) {
        this.path = path;
        this.parent = parent;
        this.children = children;
    }

    public ResourceDirectory(ResourceDirectory parent, List<ResourceNode> children) {
        this.parent = parent;
        this.children = children;
    }

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ResourceDirectory(ResourceDirectory parent) {
        this(parent, null);
    }

    public ResourceDirectory() {
        this(null, null);
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
        return children;
    }

    public void setChildren(List<ResourceNode> children) {
        this.children = children;
    }

    @Override
    public void visit(ResourceNodeProcessor resourceNodeProcessor) {
        resourceNodeProcessor.process(this);
    }

}
