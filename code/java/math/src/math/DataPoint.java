package math;

/**
 *
 */
public class DataPoint
{

   private int      _position;
   private int      _payloadSize;
   private double[] _payload;

   /**
    *
    */
   public DataPoint()
   {
      _payloadSize = 3;
      _payload = new double[ _payloadSize ];
   }

   /**
    *
    */
   public DataPoint( int num )
   {
      _payloadSize = num;
      _payload = new double[ _payloadSize ];
   }

   /**
    *
    */
   public void addPayload( double value )
   {
      _payload[ _position++ ] = value;
   }

   /**
    *
    */
   public void setPayload( double[] values )
   {
      _payload = values;
   }

   /**
    *
    */
   public double[] getPayload()
   {
      return _payload;
   }

}
