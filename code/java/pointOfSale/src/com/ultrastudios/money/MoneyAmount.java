package com.ultrastudios.money;

import java.math.*;

/**
 *
 */
public class MoneyAmount
{

   /**
    * The enum values of RoundingMode are:
    * CEILING     - Rounding mode to round towards positive infinity.
    * DOWN        - Rounding mode to round towards zero.
    * FLOOR       - Rounding mode to round towards negative infinity.
    * HALF_DOWN   - Rounding mode to round towards "nearest neighbor" unless both neighbors are equidistant, in which case round down.
    * HALF_EVEN   - Rounding mode to round towards the "nearest neighbor" unless both neighbors are equidistant, in which case, round towards the even neighbor.
    * HALF_UP     - Rounding mode to round towards "nearest neighbor" unless both neighbors are equidistant, in which case round up.
    * UNNECESSARY - Rounding mode to assert that the requested operation has an exact result, hence no rounding is necessary.
    * UP          - Rounding mode to round away from zero.
    * Use the ordinal() method to get the int value
    */
   private RoundingMode _roundingMode = RoundingMode.HALF_EVEN;

   /**
    * Special values of BigDecimal are the following.
    * ZERO
    * ONE
    * TEN
    */
   private BigDecimal   _amount;

   /**
    *
    */
   public MoneyAmount()
   {
      _amount = BigDecimal.ZERO;
   }

   /**
    *
    */
   public MoneyAmount( MoneyAmount m )
   {
      _amount       = m.getAmount();
      _roundingMode = m.getRoundingMode();
   }

   /**
    *
    */
   public MoneyAmount( BigDecimal a )
   {
      _amount = (new BigDecimal( "" + a)).setScale( 2, _roundingMode );
   }

   /**
    *
    */
   public MoneyAmount( BigDecimal a, RoundingMode r )
   {
      _roundingMode = r;
      _amount = (new BigDecimal( "" + a)).setScale( 2, _roundingMode );
   }

   /**
    *
    */
   public void setAmount( BigDecimal a )
   {
      _amount = (new BigDecimal( "" + a)).setScale( 2, _roundingMode );
   }

   /**
    *
    */
   public BigDecimal getAmount()
   {
      return _amount;
   }

   /**
    *
    * @param void
    * @return void
    */
   public void setRoundingMode( RoundingMode a )
   {
      _roundingMode = a;
      _amount = (new BigDecimal( "" + a)).setScale( 2, _roundingMode );
   }

   /**
    *
    * @param RoundingMode
    * @return RoundingMode
    */
   public RoundingMode getRoundingMode()
   {
      return _roundingMode;
   }

   /**
    *
    * @param MoneyAmount
    * @return MoneyAmount
    */
   public MoneyAmount add( MoneyAmount a )
   {
      BigDecimal t = _amount.add( a.getAmount() );
      return new MoneyAmount( t.setScale( 2, _roundingMode ) );
   }

   /**
    *
    * @param MoneyAmount
    * @return MoneyAmount
    */
   public MoneyAmount add( BigDecimal a )
   {
      BigDecimal t = _amount.add( a );
      return new MoneyAmount( t.setScale( 2, _roundingMode ) );
   }

   /**
    *
    * @param MoneyAmount
    * @return MoneyAmount
    */
   public MoneyAmount subtract( MoneyAmount a )
   {
      BigDecimal t = _amount.subtract( a.getAmount() );
      return new MoneyAmount( t.setScale( 2, _roundingMode ) );
   }

   /**
    *
    * @param MoneyAmount
    * @return MoneyAmount
    */
   public MoneyAmount subtract( BigDecimal a )
   {
      BigDecimal t = _amount.subtract( a );
      return new MoneyAmount( t.setScale( 2, _roundingMode ) );
   }

   /**
    *
    * @param MoneyAmount
    * @return MoneyAmount
    */
   public MoneyAmount multiply( MoneyAmount a )
   {
      BigDecimal t = _amount.multiply( a.getAmount() );
      return new MoneyAmount( t.setScale( 2, _roundingMode ) );
   }

   /**
    *
    * @param MoneyAmount
    * @return MoneyAmount
    */
   public MoneyAmount multiply( BigDecimal a )
   {
      BigDecimal t = _amount.multiply( a );
      return new MoneyAmount( t.setScale( 2, _roundingMode ) );
   }

   /**
    *
    * @param MoneyAmount
    * @return MoneyAmount
    */
   public MoneyAmount divide( MoneyAmount a )
   {
      BigDecimal t = _amount.divide( a.getAmount(), _roundingMode.ordinal() );
      return new MoneyAmount( t.setScale( 2, _roundingMode ) );
   }

   /**
    *
    * @param MoneyAmount
    * @return MoneyAmount
    */
   public MoneyAmount divide( BigDecimal a )
   {
      BigDecimal t = _amount.divide( a, _roundingMode.ordinal() );
      return new MoneyAmount( t.setScale( 2, _roundingMode ) );
   }

   /**
    *
    * @param MoneyAmount
    * @return MoneyAmount
    */
   public MoneyAmount markUp( BigDecimal a )
   {
      BigDecimal one_hundred = BigDecimal.TEN.multiply( BigDecimal.TEN );
      BigDecimal t = a.setScale( 32, RoundingMode.UNNECESSARY );
      BigDecimal scale = t.divide( one_hundred, _roundingMode.ordinal() );
      return multiply( scale );
   }

}
