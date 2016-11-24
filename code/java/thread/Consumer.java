package com.thread;

public class Consumer implements Runnable
{
   protected String[] theMessages;
   protected Stack    thisStack = null;
   protected Quiter   quit      = null;

   Consumer( Stack theStack )
   {
     thisStack = theStack; 
   } // end constructor

   protected void readQueue()
   {
      while( true )
      {
	 synchronized( thisStack )
	 {
            System.out.println( Thread.currentThread() + " Just regained the lock!!" );
	    try
	    {
	       if( quit.wantsToQuit() )
               {
                  System.out.println( "The Producer wants me " + Thread.currentThread() + " to QUIT!!!" );
	          return;
               } // end if

	       while( thisStack.isEmpty() )
	          thisStack.wait();

	       if( Thread.interrupted() ) // we probably don't need this.....
	       {
	          System.out.println( "Thread has been interrupted in Consumer...." );
	          throw new InterruptedException();
	       } // end if

	       System.out.println( thisStack.pop() );

	    } catch( InterruptedException e ) { System.out.println( "an exception: " + e ); return;}
	    finally
	    {
	       //System.out.println( "FINALLY: Thread has been interrupted in Consumer...." );
	    } // end finally block
	 } // end synch block
         /**
          * Note that if these lines are *inside* the synch block, control will still
          * reside in the same thread. In this scenario, control is snatched up by
          * the next available thread. In a real-world scenario, we wouldn't need this
          * yield call (most likely), but the point is that this thread must do its
          * grunt work *outside* of the synch block.
          */
         Thread.yield();
         try { Thread.sleep( 100 ); } catch( InterruptedException e ) { return; }
      } // end while
   } //end readQueue

   public void run()
   {
      readQueue();
   } // end run method

   public void setQuitVariable( Quiter qt )
   {
      this.quit = qt;
   } // end setQuitVariable

} // end Consumer
