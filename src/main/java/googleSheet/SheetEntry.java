package googleSheet;

import javafx.beans.property.SimpleStringProperty;

@SuppressWarnings( "unused" )
public class SheetEntry {

    private final SimpleStringProperty mileage;
    private final SimpleStringProperty entryDate;
    private final SimpleStringProperty entryCount;

    public SheetEntry( String entryDate , String mileage, String entryCount ) {
        this.entryDate = new SimpleStringProperty(entryDate);
        this.mileage =  new SimpleStringProperty(mileage);
        this.entryCount = new SimpleStringProperty( entryCount );
    }

    public String getEntryCount()
    { return entryCount.get(); }

    public SimpleStringProperty entryCountProperty()
    { return entryCount; }

    public void setEntryCount( String entryCount )
    { this.entryCount.set( entryCount ); }

    public String getEntryDate()
    { return entryDate.get(); }

    public SimpleStringProperty entryDateProperty()
    { return entryDate; }

    public void setEntryDate( String entryDate )
    { this.entryDate.set( entryDate ); }

    public String getMileage()
    { return mileage.get(); }

    public SimpleStringProperty mileageProperty()
    { return mileage; }

    public void setMileage( String mileage )
    { this.mileage.set( mileage ); }
}
