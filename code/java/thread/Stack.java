package com.thread;

import java.util.Vector;
public class Stack
{
   protected Vector theStack         = null;
   protected String[] theStringStack = null;
   protected int theLength           = 0; 

   public Stack( Object obj )
   {
      if ( obj == null || !(obj instanceof String) )
	 return;
      theStack = new Vector();
      AddIt( obj );
   } // end constructor

   /**
    * We actually don't need the synchonized methods
    * cause the Consumers are the only ones MT'd
    * and they synch the *whole* Stack object.
    * Well, we probably need it on push, but that's
    * it. 'cause when the Consumer releases the synch,
    * the producer will acquire it in the push method
    * then the producer can't get it until control exits
    * push.
    */
   protected /*synchronized*/ void AddIt( Object obj )
   {
      theStack.addElement( obj );
      theLength = 0;
   } // end AddIt

   public synchronized void push( Object obj )
   {
      System.out.println( "Push: " + Thread.currentThread() );
      //Thread.yield();  This doesn't work as expected inside a synch block.
      //                 Because this thread will yield to the others, but
      //                 since we hold the lock on this object, no other thread
      //                 will be able to get in anywary.
      if ( obj == null || !(obj instanceof String) )
	 return;
      theLength++;
      theStack.addElement( obj );
      notifyAll(); // does this work inside a synch'd block? Yes it does.
      // In fact, the notifyAll *must* be performed insided a synch block
      // because otherwise this object must hold a lock on this object
      // before it can signalAll. The Consumers could have the lock, thus
      // when a notifyAll is performed, an illegalMonitorState exception
      // will be thrown.
   } // end push

   public /*synchronized*/ Object pop()
   {
      if ( theLength == -1 )
	 return null;

      System.out.println( "Pop: " + Thread.currentThread() );
      Object temp = theStack.elementAt( theLength );
      theStack.remove( theLength );
      theLength--;

      return temp;
   } // end pop

   public boolean isEmpty()
   {
      if ( theLength <= -1 )
	 return true;
      else
	 return false;
   } // end isEmpty
} // end Stack
