#include <cstdlib>
#include <iostream>
#include "LinkParser.hpp"
#include "Sentence.hpp"
#include "SentenceIterator.hpp"
#include "SentenceIteratorListener.hpp"
#include "SemanticDifferentialDataSource.hpp"

using namespace std;


int main(int argc, char *argv[])
{
   SentencE* theSentence;
   LinkParseR* theParser = new LinkParseR();

   //theSentence = theParser->ParseSentence( "Bogus chatter." );
   //theSentence = theParser->ParseSentence( "Here's a biggish sentence that may be big, but not big enough to get the error that we see when the sentence is too big, but we may want to make it even larger so that we do, in fact, see the error that we get when the sentence is rather, or extremely, large in size and girth." );
   theSentence = theParser->ParseSentence( "A sentinel at each end of the bridge stood with his rifle in the position known as \"support,\" that is to say, vertical in front of the left shoulder, the hammer resting on the forearm thrown straight across the chest." );
   SentenceIteratoR* iterator = new SentenceIteratoR( theSentence );

   iterator->SetListener( new SentenceIteratorListeneR() );
   iterator->Iterate();
    
   theSentence->WriteToDisk();

   delete iterator;
   delete theSentence;

/*
   cout << "about to parse next sentence..." << endl;

//    theSentence = theParser->ParseSentence( "This is a test of the parsing system, which will be quite robust." );
   theSentence = theParser->ParseSentence( "I'm happy." );
   iterator = new SentenceIteratoR( theSentence );

   iterator->SetListener( new SentenceIteratorListeneR() );
   iterator->Iterate();

   theSentence->WriteToDisk();

   delete iterator;
   delete theSentence;
   */

   // This must always be done!!!
   delete SemanticDifferentialDataSourcE::GetInstance();

   delete theParser;    
}
