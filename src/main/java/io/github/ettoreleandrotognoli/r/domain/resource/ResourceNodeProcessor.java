package io.github.ettoreleandrotognoli.r.domain.resource;

public interface ResourceNodeProcessor {

    void process(ResourceDirectory resourceDirectory);

    void process(ResourceFile resourceFile);

    default void processTree(ResourceNode node) {
        node.visit(this);
        for (ResourceNode child : node.getChildren()) {
            processTree(child);
        }
    }

}
