import java.util.*;
import java.io.*;

public class Tester
{
 
   public Tester()
   {
   }

   public static void main( String[] args )
   {
      StockCollection theCollection = new StockCollection();
      StockCollectionLoader theLoader = new StockCollectionLoader();
      theLoader.loadUpdate( theCollection );

      Collection VarColl = theCollection.getSortedVarianceCollection();
      Iterator   theIter = VarColl.iterator();
      int rank = 500;

      while( theIter.hasNext() )
      {
         StockElement theElem = (StockElement)theIter.next();
         theElem.setVarianceRank( rank-- );
      } // end while

      Collection StabColl = theCollection.getSortedStabilityCollection();
      theIter = StabColl.iterator();
      rank = 500;

      while( theIter.hasNext() )
      {
         StockElement theElem = (StockElement)theIter.next();
         theElem.setStabilityRank( rank-- );
      } // end while

      Collection theColl = theCollection.getStockElementCollection();
      theIter = theColl.iterator();
      StringBuffer str = null;
      BufferedWriter output = null;

      try {
         output = new BufferedWriter( new FileWriter( "output.rank" ) );

         while( theIter.hasNext() )
         {
            StockElement theElem = (StockElement)theIter.next();
            str = new StringBuffer( theElem.getName() );
            str.append( " " ).append( theElem.getVarianceRank() ).append( " " );;
            str.append( theElem.getStabilityRank() );
            output.write( str.toString() );
            output.newLine();
         } // end while
         output.flush();
         output.close();
      } catch( IOException e ) {}

      //theCollection.saveData();

   } // end main

} // end Tester
