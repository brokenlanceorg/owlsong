package common;

public class LoadEnum
{
   int _enumerationValue = 0;

   public static final LoadEnum LOAD_LAST      = new LoadEnum( 1 );
   public static final LoadEnum LOAD_ALL       = new LoadEnum( 2 );
   public static final LoadEnum LOAD_ALL_YEAR  = new LoadEnum( 3 );

   private LoadEnum( int enumValue )
   {
      _enumerationValue = enumValue;
   } // end LoadEnum

   protected int getEnumerationValue()
   {
      return _enumerationValue;
   } // end getEnumValue

   public boolean equals( Object o )
   {
      if( o instanceof LoadEnum )
      {
         if( _enumerationValue == ((LoadEnum)o).getEnumerationValue() )
            return true;
      } // end if
      return false;
   } // end equals

} // end LoadEnum
