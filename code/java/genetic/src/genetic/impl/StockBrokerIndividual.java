package genetic.impl;

import stock.*;
import genetic.*;
import fuzzy.*;
import java.math.*;
import java.util.*;

/**
 * Yet another doomed automata broker...
 */
public class StockBrokerIndividual extends Individual
{

   private int _shares = 0;
   private double _cash = 10000;

   /**
    * Default constructor.
    */
   public StockBrokerIndividual()
   {
      _myAge = 0;
      _myFitness = 0;
   }

   /**
    * Implementation of Interface.
    */
   public Individual clone()
   {
      return new StockBrokerIndividual();
   }

   /**
    * Implementation of Interface.
    */
   public void randomizeGenome()
   {
      for( int i=0; i<(32); i++ )
      {
         _myGenotype.add( new Double( Math.random() ) );
      }
   }

   /**
    * Implementation of Interface.
    * Does the genotype contain the observables?
    * No because it could grow it beyond reason.
    * We'll consider the last 20 days of the data
    * series. We'll also only consider square systems
    * Not sure if it can really handle rectangular 
    * systems. Actually it can handle it, but let's
    * do square first.
    *
    *
    */
   public double evaluateFitness()
   {
      ArrayList<String> names = getEnvironmentCache().getStockNames();
      ArrayList<Double> trials = new ArrayList<Double>();
      ArrayList<Double> sets = new ArrayList<Double>();
      ArrayList<Double> spec = new ArrayList<Double>();
      StockElement theStock = null;
      FuzzyRule rule = null;
      int numTrials = 10;
      int numSets = 4;
      int numObs = 4;
      int stop = 0;
      int pos = 0;

      // The genotype is interpreted in the following manner:
      // Sets in the first chromosome and the spec in
      // the second chromosome
      // 4 genes to a fuzzy set, and the spec has size
      // fuzzy sets * observables
      for( int i=0; i<(numSets * 4); i++ )
      {
         sets.add( _myGenotype.get( pos++ ) );
      }
      for( int i=0; i<(numSets * numObs); i++ )
      {
         spec.add( _myGenotype.get( pos++ ) );
      }
      rule = new FuzzyRule( sets, spec );

      // We'll do the entire scenario numTrials times
      for( int i=0; i<numTrials; i++ )
      {
         double rand = Math.random() * names.size();
         theStock = getEnvironmentCache().getStockElement( names.get( (int)rand ) );
         ArrayList<Double> closes = theStock.getAllCloses();
         // change this to change where we train in the data series
         stop = closes.size();

         if( stop <= (2 * numObs) )
         {
            continue;
         }

         double previous = closes.get( stop - numObs - 1 );
         double ratio = 0;
         double current = 0;

         // go through the last numObs stock values
         for( int j=(stop-numObs); j<stop; j++ )
         {
            ArrayList<Double> obs = new ArrayList<Double>();
            for( int k=j-numObs; k<j; k++ )
            {
               current = closes.get( k );
               ratio = current / previous;
               ratio -= 0.5;
               obs.add( ratio );
               previous = current;
            }

            double buy = rule.truthValue( obs );
            int shares = 0;
            _shares = 0;
            _cash = 10000;

            // buy case
            if( buy > 0.5 )
            {
               if( _cash > 0 )
               {
                  int maxShares = (int)( _cash / current );
                  double temp = 2 * ( buy - 0.5 );
                  shares = (int)( temp * maxShares );
                  _shares += shares;
                  _cash -= ( shares * current );
               }
            }
            // sell case
            else
            {
               shares = (int)( buy * 2 * _shares );
               if( shares > _shares )
               {
                  _cash += (_shares * current);
                  _shares = 0;
               }
               else
               {
                  _shares -= shares;
                  _cash += (_shares * current);
               }
            }
         }
         trials.add( new Double( _cash ) );
      }

      // out of our trials, we'll select the median as our official fitness
      Collections.sort( trials );
      _myFitness = trials.get( (int)(trials.size() / 2) );

      return _myFitness;
   }

}
