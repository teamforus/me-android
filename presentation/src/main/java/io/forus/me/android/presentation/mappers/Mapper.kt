package io.forus.me.android.presentation.mappers

import java.util.*

abstract class Mapper<DomainModel, PresenterModel> {

    /**
     * Transform a [DomainModel] into an [PresenterModel].
     *
     * @param domainModel Object to be transformed.
     * @return [PresenterModel].
     */
    abstract fun transform(domainModel: DomainModel): PresenterModel

    /**
     * Transform a Collection of [DomainModel] into a Collection of [PresenterModel].
     *
     * @param domainModelCollection Objects to be transformed.
     * @return List of [PresenterModel].
     */
    fun transform(domainModelCollection: Collection<DomainModel>?): Collection<PresenterModel> {
        val presenterModelCollection: MutableCollection<PresenterModel>

        if (domainModelCollection != null && !domainModelCollection.isEmpty()) {
            presenterModelCollection = ArrayList()
            for (domainModel in domainModelCollection) {
                presenterModelCollection.add(transform(domainModel))
            }
        } else {
            presenterModelCollection = Collections.emptyList()
        }

        return presenterModelCollection
    }
}