package com.thread;

public class Quiter
{
   protected boolean wantsToQuit = false;

   public Quiter()
   {
   } // end constructor

   public void quit()
   {
      wantsToQuit = true;
   } // end quit

   public boolean wantsToQuit()
   {
      return wantsToQuit;
   } // end wantsToQuit
} // end Quiter
