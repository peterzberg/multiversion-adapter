package dev.zberg.quarkus.multiversion.deployment;

import dev.zberg.quarkus.multiversion.runtime.Multiversioned;
import io.quarkus.arc.deployment.UnremovableBeanBuildItem;
import io.quarkus.arc.deployment.UnremovableBeanBuildItem.BeanClassNameExclusion;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class MultiversionProcessor {

    private static final String FEATURE = "multiversion";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void markImplementorsAsUnremovable(CombinedIndexBuildItem combinedIndexBuildItem, BuildProducer<UnremovableBeanBuildItem> unremovableBeanBuildItemBuildProducer) {
        combinedIndexBuildItem.getIndex().getAllKnownImplementations(Multiversioned.class)
                .stream()
                .filter(c -> !c.isAbstract())
                .filter(c -> !c.isInterface())
                .map(ci -> ci.name().toString())
                .map(BeanClassNameExclusion::new)
                .map(UnremovableBeanBuildItem::new)
                .forEach(unremovableBeanBuildItemBuildProducer::produce);

    }

}
