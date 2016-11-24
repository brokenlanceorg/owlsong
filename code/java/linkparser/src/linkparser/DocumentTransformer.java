package linkparser;


/**
 *  
 *  
 */
public class DocumentTransformer implements ISentenceParserConstants
{

   private StringBuffer _document;

   /**
    * Default
    */
   public DocumentTransformer()
   {
   }

   /**
    * 
    */
   public DocumentTransformer( StringBuffer document )
   {
      _document = document;
      transformDocument();
   }

   /**
    * 
    */
   public String getDocument()
   {
      if( _document != null )
      {
         return _document.toString();
      }

      return null;
   }

   /**
    * 
    */
   protected void transformSingleQuotes( char punctuation )
   {
      int begin = 0;

      for( int i=0; i<_document.length(); i++ )
      {
         if( _document.charAt( i ) == '\'' )
         {
            if( i > 0 && _document.charAt( i - 1 ) == punctuation )
            {
               _document.replace( i, i + 1, "\"" );
               _document.replace( begin, begin + 1, "\"" );
            }
            else
            {
               begin = i;
            }
         }
      }
   }

   /**
    * 
    */
   public void transformSingleQuotes()
   {
      transformSingleQuotes( '.' );
      transformSingleQuotes( '?' );
      transformSingleQuotes( '!' );
      transformSingleQuotes( ',' );
   }

   /**
    * 
    */
   public void transformSemiColons()
   {
      int i = _document.indexOf( "; " );
      while( i != -1 )
      {
         if( (i > 0) && (i < _document.length() - 2) )
         {
            _document.replace( i, i + 2, ". " );
         }

         i = _document.indexOf( "; ", i + 1 );
      }
   }

   /**
    * 
    */
   public void transformEmDashes()
   {
      int i = _document.indexOf( "--" );
      while( i != -1 )
      {
         if( (i > 0) && (i < _document.length() - 2) )
         {
            if( Character.isLetter( _document.charAt( i - 1 ) ) &&
                Character.isLetter( _document.charAt( i + 2 ) ) )
            {
               _document.replace( i, i + 2, "; " );
            }
            else if( _document.charAt( i - 1 ) == '?'  ||
                     _document.charAt( i - 1 ) == '!'  ||
                     _document.charAt( i - 1 ) == '.'  )
            {
               _document.replace( i, i + 2, " " );
            }
            else 
            {
               System.out.println( "Didn't translate an EmDash!" );
            }
         }

         i = _document.indexOf( "--", i + 1 );
      }
   }

   /**
    * 
    *  This is the main driver method.
    * 
    */
   public void transformDocument()
   {
      if( _document == null )
      {
         return;
      }

      transformEmDashes();
      transformSemiColons();
      transformSingleQuotes();
   }

}
