package googleSheet;

import javafx.beans.property.SimpleStringProperty;

public class SheetEntry {
    private final SimpleStringProperty entryDate;
    private final SimpleStringProperty mileage;

    public SheetEntry( String entryDate , String mileage ) {
        this.entryDate = new SimpleStringProperty(entryDate);
        this.mileage =  new SimpleStringProperty(mileage);
    }

    public String getEntryDate() {
        return entryDate.get();
    }

    public SimpleStringProperty entryDateProperty() {
        return entryDate;
    }

    public void setEntryDate( String entryDate ) {
        this.entryDate.set( entryDate );
    }

    public String getMileage() {
        return mileage.get();
    }

    public SimpleStringProperty mileageProperty() {
        return mileage;
    }

    public void setMileage( String mileage ) {
        this.mileage.set( mileage );
    }
}
