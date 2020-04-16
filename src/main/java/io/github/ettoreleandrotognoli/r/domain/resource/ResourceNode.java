package io.github.ettoreleandrotognoli.r.domain.resource;

import java.util.List;

public interface ResourceNode {

    String getPath();
    ResourceDirectory getParent();
    List<ResourceNode> getChildren();

    void visit(ResourceNodeProcessor resourceNodeProcessor);

}
