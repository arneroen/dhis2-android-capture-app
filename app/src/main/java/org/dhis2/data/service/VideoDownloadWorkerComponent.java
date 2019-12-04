package org.dhis2.data.service;

import androidx.annotation.NonNull;

import org.dhis2.data.dagger.PerService;

import dagger.Subcomponent;

@PerService
@Subcomponent(modules = ReservedValuesWorkerModule.class)
public interface VideoDownloadWorkerComponent {
    void inject(@NonNull VideoDownloadWorker videoDownloadWorker);
}
