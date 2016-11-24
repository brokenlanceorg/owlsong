package com.thread;

import java.io.*;
import java.util.Hashtable;

public class testClient
{
   /**
    * Our member variables:
    *
    */
   protected Stack     theStack     = null;
   protected Consumer  theConsumer1 = null;
   protected Consumer  theConsumer2 = null;
   protected Consumer  theConsumer3 = null;
   protected Consumer  theConsumer4 = null;
   protected Consumer  theConsumer5 = null;
   protected Quiter    amDone       = null; 
   protected Hashtable _theHash     = null;
   private   boolean   doQuit       = false;

   public testClient()
   {
      doQuit = Boolean.getBoolean( "doQuit" );
      System.out.println( "doQuit is: " + doQuit );
   } // end constructor

   protected void beginTest()
   {
      _theHash = new Hashtable();

      try {
      /**
        Turning and turning in the widening gyre
        The falcon cannot hear the falconer
        Things fall apart the centre cannot hold
        Mere anarchy is losed upon the world
        The blood-dimmed tide is loosed
        And everywhere the ceremony of innocense is drowned
        The best lack all conviction
        While the worst are full of pasionate intensity
        Surely some revelation is at hand
        Surely the Second Coming is at hand
        The Second Coming!
        Hardly those words out when a vast image out of Spiritus Mundi troubles my sight:
        Somewhere in the sands of the desert
        A shape with lion's body and the head of a man
        A gaze blank and pitiless as the sun
        Is moving its slow thighs while all about it reel shadows of the indignant desert birds
        The darkness drops again
        But now I know that twenty centuries of stony sleep 
        Were vexed to nightmare by a rocking cradle
        And what rough beast, its hour come round at last slouches toward Bethlehem to be born?

        Still round the corner there may wait
        A new road or secret gate;
        And though I oft have passed them by,
        A day will come at last when I
        Shall take the hidden paths that run
        West of the Moon, East of the Sun.
      */
      String aStr1 = new String( "The road goes ever on and on" );
      String aStr2 = new String( "Down from the door where it began" );
      String aStr3 = new String( "Now far ahead the road has gone" );
      String aStr4 = new String( "And I must follow if I can" );
      String aStr5 = new String( "Pursuing it with eager feet" );
      String aStr6 = new String( "Until it joins some larger way" );
      String aStr7 = new String( "Where many paths and errands meet" );
      String aStr8 = new String( "And whither then? I can not say." );

      String aStr9  = new String( "All that is gold does not glitter" );
      String aStr10 = new String( "Not all who wander are lost" );
      String aStr11 = new String( "The old that is strong does not whither" );
      String aStr12 = new String( "Deep roots are not reached by the frost" );
      String aStr13 = new String( "From the ashes a fire shall be woken" );
      String aStr14 = new String( "In the shadows a light shall spring" );
      String aStr15 = new String( "Renewed shall be blade that was broken" );
      String aStr16 = new String( "The crownless again shall be king." );

      theStack = new Stack( aStr8 );
      amDone = new Quiter();

      theConsumer1 = new Consumer( theStack );
      theConsumer1.setQuitVariable( amDone );
      theConsumer2 = new Consumer( theStack );
      theConsumer2.setQuitVariable( amDone );
      theConsumer3 = new Consumer( theStack );
      theConsumer3.setQuitVariable( amDone );
      theConsumer4 = new Consumer( theStack );
      theConsumer4.setQuitVariable( amDone );
      theConsumer5 = new Consumer( theStack );
      theConsumer5.setQuitVariable( amDone );

      Thread aThread1 = new Thread( theConsumer1, "Thread Number 1" ); // thread name for debug info
      Thread aThread2 = new Thread( theConsumer2, "Thread Number 2" );
      Thread aThread3 = new Thread( theConsumer3, "Thread Number 3" );
      Thread aThread4 = new Thread( theConsumer4, "Thread Number 4" );
      Thread aThread5 = new Thread( theConsumer5, "Thread Number 5" );

      _theHash.put( "thread number 1", aThread1  );
      _theHash.put( "thread number 2", aThread2 );
      _theHash.put( "thread number 3", aThread3 );
      _theHash.put( "thread number 4", aThread4 );
      _theHash.put( "thread number 5", aThread5 );

      System.out.println( Thread.currentThread() );
      long t1 = System.currentTimeMillis();

      aThread1.start();
      aThread2.start();
      aThread3.start();
      aThread4.start();
      aThread5.start();

      theStack.push( aStr7 );
      theStack.push( aStr6 );
      theStack.push( aStr5 );
      theStack.push( aStr4 );
      theStack.push( aStr3 );
      theStack.push( aStr2 );
      theStack.push( aStr1 );

      Thread.yield();
      Thread.sleep( 1000 );

      // If this id done, then the Consumers will never empty the queue:
      if( doQuit )
         amDone.quit();

      theStack.push( aStr16 );
      theStack.push( aStr15 );
      theStack.push( aStr14 );
      theStack.push( aStr13 );
      theStack.push( aStr12 );
      theStack.push( aStr11 );
      theStack.push( aStr10 );
      theStack.push( aStr9 );

      Thread.yield();
      Thread.sleep( 1000 );

      theStack.push( aStr7 );
      theStack.push( aStr6 );
      theStack.push( aStr5 );
      theStack.push( aStr4 );
      theStack.push( aStr3 );
      theStack.push( aStr2 );
      theStack.push( aStr1 );

      Thread.yield();
      Thread.sleep( 1000 );

      theStack.push( aStr16 );
      theStack.push( aStr15 );
      theStack.push( aStr14 );
      theStack.push( aStr13 );
      theStack.push( aStr12 );
      theStack.push( aStr11 );
      theStack.push( aStr10 );
      theStack.push( aStr9 );

      // If doQuit is true, we will never have an empty queue, and thus
      // we will wait here forever.
      while( !theStack.isEmpty() )
      {
         Thread.yield();
         System.out.print( "." );
      } // end while

      Thread.sleep( 1000 );
      //Thread.yield();

      ((Thread) _theHash.get( "thread number 1" )).interrupt();
      ((Thread) _theHash.get( "thread number 2" )).interrupt();
      ((Thread) _theHash.get( "thread number 3" )).interrupt();
      ((Thread) _theHash.get( "thread number 4" )).interrupt();
      ((Thread) _theHash.get( "thread number 5" )).interrupt();

      aThread1.join();
      System.out.println( "Joined with 1: " + System.currentTimeMillis() );

      aThread2.join();
      System.out.println( "Joined with 2: " + System.currentTimeMillis() );

      aThread3.join();
      System.out.println( "Joined with 3: " + System.currentTimeMillis() );

      aThread4.join();
      System.out.println( "Joined with 4: " + System.currentTimeMillis() );

      aThread5.join();
      System.out.println( "Joined with 5: " + System.currentTimeMillis() );

      System.out.println( "Joined up with All of the threads......" );
      System.out.println( "Total time was: " + (System.currentTimeMillis() - t1) );

      } catch( InterruptedException e ) {}

   } // end beginTest

   public static void main( String[] args )
   {
      testClient theClient = new testClient();
      theClient.beginTest();
   } // end main

} // end theClient class
