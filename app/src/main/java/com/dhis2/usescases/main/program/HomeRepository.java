package com.dhis2.usescases.main.program;

import android.support.annotation.NonNull;

import org.hisp.dhis.android.core.organisationunit.OrganisationUnitModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

interface HomeRepository {

    @NonNull
    Observable<List<HomeViewModel>> homeViewModels();

    @NonNull
    Observable<List<HomeViewModel>> homeViewModels(ArrayList<String> query);

    @NonNull
    Observable<List<OrganisationUnitModel>> orgUnits();

}