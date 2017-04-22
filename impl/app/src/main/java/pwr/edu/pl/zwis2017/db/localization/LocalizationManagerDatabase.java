package pwr.edu.pl.zwis2017.db.localization;

import android.content.Context;

import pwr.edu.pl.zwis2017.db.localization.primary.OptionsDb;
import pwr.edu.pl.zwis2017.db.localization.saved.LocalizationDb;

public class LocalizationManagerDatabase {

    private final OptionsDb optionsDb;
    private final LocalizationDb localizationDb;
    private static final String UNKNOWN_LOCALIZATION = "Nieznana";
    private static final LocalizationWithCityNamer LOCALIZATION_WITH_CITY_NAME = new LocalizationWithCityNamer();


    public LocalizationManagerDatabase(Context context) {
        this.optionsDb = new OptionsDb(context);
        this.localizationDb = new LocalizationDb(context);
    }

    public String[] getAllLocalizations() {
        return localizationDb.getAllLocalizations();
    }

    public void remove(String localization) {
        removeLocalization(localization);
        setRandomPrimaryLocalizationFromAllLocalizations();
    }

    private void setRandomPrimaryLocalizationFromAllLocalizations() {
        String[] allLocalizations = getAllLocalizations();
        if (allLocalizations.length != 0 && isPrimaryLocalizationEmpty()) {
            optionsDb.setPrimaryLocalization(allLocalizations[0]);
        }
    }

    private boolean isPrimaryLocalizationEmpty() {
        return optionsDb.getOptionProject() == null;
    }

    private void removeLocalization(String localization) {
        if (localization.equals(optionsDb.getOptionProject())) {
            optionsDb.removePrimaryLocalization();
        }
        localizationDb.remove(localization);
    }

    public String getPrimaryLocalization() {
        String localization = this.optionsDb.getOptionProject();
        return localization == null ? UNKNOWN_LOCALIZATION : localization;
    }

    public boolean rememberLocalization(String localization) {
        if (!localizationDb.doesLocalizationExist(localization)) {
            localizationDb.saveLocalization(localization);
            setPrimaryLocalization(localization);
            return true;
        }
        return false;
    }


    public void setPrimaryLocalization(String localization) {
        if (!isPrimaryLocalizationEmpty()) {
            optionsDb.removePrimaryLocalization();
        }
        optionsDb.setPrimaryLocalization(localization);
    }

}
