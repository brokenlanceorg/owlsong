package genetic.impl;

import genetic.*;
import math.*;

import java.math.*;
import java.util.*;

/**
 * 
 */
public class PowerSetIndividual extends Individual
{

   /**
    * Default constructor.
      Genome: 
      0.004722463819316736 
      0.22850670427198183 
      0.22198984811892408 
      0.0484601555674824 
      0.869130455489564 
      0.09147158684634982 
      0.05613826664870114 
      0.6010224564977841 
      0.621987042449111 
      0.26872507092483855 
      0.64574966232395 
      0.30418661171718964 
      0.1569590176793949 
      0.050327248349840814 
      0.8084064702679509 
      0.7408992700007442 
      0.1346645755595941 
      0.7528857504485833 
      0.9917047595861581 
      0.1484018421099782 
      0.6419165672573829 
      0.802228110857784 
      0.5244189363171256 
      0.34789472919202624 
      0.16319336155577746 
      0.9518254311971763 
      0.5443551614375873 
      0.9022775916990086 
      0.9522687962479005 
      0.45518897081597776 
      0.666791888445769 
      0.33353532124655705 
      0.7877780569397076 
      0.6895703774474279 
      0.9096118616355584 
      0.6127620561540169 
      0.40561322426078705 
      0.807969160375656 
      0.874724041784659 
      0.1068535263971705 
      0.8726783718977849 
      0.013864958347204914 
      0.2702338494477107 
      0.5854563651439687 
      0.07293246029888267 
      0.524774495372194 
      0.5186116686750014 
      0.438887955675639 
      0.27677563020686424 
      0.32335572629399134 
      0.06276553298359455 
      0.5485640798421335 
      0.6033677730031528 
      0.20455604967713348 
      0.11437340943010987 
      0.03291227990584722 
      0.39365780566965325 
      0.25456766904666206 
      0.31181718256090096 
      0.5574000401562108 
      0.7067224729237652 
      0.43203553022645746 
      0.6858162648055549 
      0.8997760144477206 
      0.02698399850214328 
      0.36674562200139893 
      0.8648418329324284 
      0.5390637234692548 
      0.2992485285845261 
      0.01392788647332932 
      0.7353273684449244 
      0.4138830807715087 
      0.8715416738983011 
      0.3326334925738691 
      0.06155195994483309 
      0.3765607351422591 
      0.3387980995477686 
      0.7397811037643828 
      0.3412789076319883 
      0.9332340209909649 
      0.21169063776473884 
      0.8117291346317107 
      0.08296318539385439 
      0.410739913587975 
      0.41834800411908235 
      0.6540272298333017 
      0.8204732216158168 
      0.0415074542131042 
      0.3519652629285971 
      0.3322327105270878 
      0.05513495659762424 
      0.5550467585774267 
      0.5341201325026304 
      0.43607222884220453 
      0.34992355908050765 
      0.6888167363300757 
      0.6483542479034936 
      0.052485021419288236 
      0.4532569470914014 
      0.15479398834200642 
      0.40159898799511484 
      0.44056258272002313 
      0.6848726468143729 
      0.45596885641717577 
      0.3483813944978089 
      0.4242716056835084 
      0.6475245372554 
      0.6683562424473067 
      0.36125770679482494 
      0.18564767797811121 
      0.5546845359820007 
      0.13701383277170676 
      0.18056633203155914 
      0.43901953973400887 
      0.4565635946260329 
      0.47322226968431225 
      0.8457032660420937 
      0.920494158047501 
      0.032980017708431064 
      0.0037200639195582585 
      0.66225638797995 
      0.08011366834365752 
      0.11291737177068095 
      0.478142875650173 
      0.3414276179984723 
      0.2771153555431779 
      0.7926628317454193 
      0.9645339863131078 
      0.041415271205855086 
      0.7772036211560902 
      0.7106439015496044 
      0.8701388668434482 
      0.7149560924970834 
      0.6973926044218476 
      0.2436838769392229 
      0.856485739947513 
      0.5916858371572132 
      0.5207749796045742 
      0.49526567434479885 
      0.8682958276989041 
      0.029342544940780035 
      0.6897618149781096 
      0.34076928299370723 
      0.18137007335407374 
      0.03795985188655637 
      0.6689619969801504 
      0.5472546584442605 
      0.8371822055132998 
      0.40651682966282054 
      0.7011601960194808 
      0.17519325296765675 
      0.8688332785818665 
      0.39624873887408885 
      0.24026434725018886 
      0.005918136234805971 
    */
   public PowerSetIndividual()
   {
   }

   /**
    * Implementation of Interface.
    * This is the proper implementation, so please take note.
    */
   public Individual clone()
   {
      Individual indy = new PowerSetIndividual();

      if( _myGenotype == null || _myGenotype.size() == 0 )
      {
         indy.randomizeGenome();
      }
      else
      {
         // Make a deep copy of the genotype
         ArrayList<Double> genotype = new ArrayList<Double>();
         for( double gene : _myGenotype )
         {
            genotype.add( gene );
         }
         indy.setGenotype( genotype );
      }

      return indy;
   }

   /**
    * Implementation of Interface.
    * A power set transform of 5 elements gives 155 elements.
    */
   public void randomizeGenome()
   {
      _myGenotype = new ArrayList<Double>();
      _myGenotype.add( Math.random() );
      _myGenotype.add( Math.random() );
      _myGenotype.add( Math.random() );
      _myGenotype.add( Math.random() );
      _myGenotype.add( Math.random() );
   }

   /**
    * Implementation of Interface.
    *
    *
    */
   public double evaluateFitness()
   {
      ArrayList<Double> target = getEnvironmentCache().getPowerSet();
      ArrayList<Double> values = (new PowerSetTransform()).calculateTransform( _myGenotype );
      Collections.sort( values );

      for( int i=0; i<target.size(); i++ )
      {
         _myFitness += Math.abs( target.get( i ) - values.get( i ) );
      }

      if( _myFitness != 0 )
      {
         _myFitness = 1 / _myFitness;
      }
      else
      {
         _myFitness = Double.MAX_VALUE;
      }

      return _myFitness;
   }

   /**
    *
    */
   public String toString()
   {
      ArrayList<Double> values = (new PowerSetTransform()).calculateTransform( _myGenotype );
      Collections.sort( values );
      String result = "";

      result += "\nGenome: \n";
      for( double gene : _myGenotype )
      {
         result += gene + " ";
      }

      double[] unsorted = new double[ values.size() ];
      ArrayList< Short > order = getEnvironmentCache().getSortOrder();
      for( int i=0; i<values.size(); i++)
      {
         double temp = values.get( i );
         short pos = order.get( i );
         unsorted[ pos ] = temp;
      }

      result += "\nValues prediction: \n";
      for( double temp : unsorted )
      {
         result += "\n" + temp;
      }

      result += "\nValues prediction line: \n";
      for( double temp : unsorted )
      {
         result += " " + temp;
      }

      result += "\nFitness: " + _myFitness + "\n";

      return result;
   }
}
