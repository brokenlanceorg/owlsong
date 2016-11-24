//---------------------------------------------------------------------------
#ifndef NewtonUnit1H
#define NewtonUnit1H
//---------------------------------------------------------------------------
#include <vcl\Classes.hpp>
#include <vcl\Controls.hpp>
#include <vcl\StdCtrls.hpp>
#include <vcl\Forms.hpp>
#include <vcl\ExtCtrls.hpp>
#include <vcl\ComCtrls.hpp>
#include <vcl\Buttons.hpp>
#include <vcl\Menus.hpp>
#include <vcl\Dialogs.hpp>
#include <vcl\registry.hpp>
//---------------------------------------------------------------------------
#include"newton.hpp" // Declares the Newton method class
#include"unit3.h" // Declares the Newton method class
#include"unit4.h" // Declares the Newton method class
#include"Unit5.h"
#include <Graphics.hpp>
//---------------------------------------------------------------------------
class TForm1 : public TForm
{
__published:   // IDE-managed Components
   TImage *Image1;
   TStatusBar *StatusBar1;
   TProgressBar *ProgressBar1;
   TBitBtn *BitBtn1;
   TBitBtn *BitBtn2;
   TMainMenu *MainMenu1;
   TMenuItem *File1;
   TMenuItem *SetOutputFile1;
   TMenuItem *SetErrorLogFile1;
   TMenuItem *Help1;
   TMenuItem *About1;
   TMenuItem *Help2;
   TSaveDialog *SaveDialog1;
   TComboBox *ComboBox1;
   TComboBox *ComboBox2;
   TGroupBox *GroupBox1;
   TGroupBox *GroupBox2;
   TGroupBox *GroupBox3;
   TEdit *Edit1;
   TEdit *Edit2;
   TEdit *Edit3;
   TEdit *Edit4;
   TEdit *Edit5;
   TEdit *Edit6;
   TLabel *Label1;
   TLabel *Label2;
   TLabel *Label3;
   TLabel *Label4;
   TLabel *Label5;
   TLabel *Label6;
   TUpDown *UpDown1;
   TUpDown *UpDown2;
   TUpDown *UpDown3;
   TUpDown *UpDown4;
   TUpDown *UpDown5;
   TUpDown *UpDown6;
   TEdit *Edit7;
   TLabel *Label7;
   TEdit *Edit8;
   TEdit *Edit9;
   TLabel *Label8;
   TLabel *Label9;
   TMenuItem *Options1;
   TMenuItem *Image2;
   TUpDown *UpDown7;
   TUpDown *UpDown8;
   TCheckBox *CheckBox1;
   TMenuItem *ChangePallete1;
   void __fastcall FormClose(TObject *Sender, TCloseAction &Action);
   
   void __fastcall SetOutputFile1Click(TObject *Sender);
   void __fastcall SetErrorLogFile1Click(TObject *Sender);
   
   void __fastcall Image2Click(TObject *Sender);
   
   
   void __fastcall BitBtn1Click(TObject *Sender);
   void __fastcall BitBtn2Click(TObject *Sender);
   void __fastcall About1Click(TObject *Sender);
   void __fastcall Help2Click(TObject *Sender);
   void __fastcall ChangePallete1Click(TObject *Sender);
private: // User declarations
   int iIsInitialized;  // To test if the user has pressed the 'Initialize' button
   int iFDepth;         // The color depth of graph window images
   int iEquation;       // Contains the number of the equation we're currently using
   int iDerivative;     // Contains the number of the derivative we're currently using
   int iBlue;           // Blue color component
   int iGreen;          // Green color component
   int iRed;            // Red color component
   bool bColor_Change;  // Did color pallete change?

protected:
   void __fastcall Setup(); // Setup stuff
   AnsiString* OutputFile; // To hold the name of the Targa File
   AnsiString* ErrorFile; // To hold the name of the Numerical Errors File
   NewtoN* NewtonSolver;   // Our NewtoN object
   void __fastcall GraphIt(); // Setup stuff
   int GetColor(long);     // Get our color value
      
public:     // User declarations
   __fastcall TForm1(TComponent* Owner);
   long double __fastcall GetIntervalValueX(int);
   long double __fastcall GetIntervalValueY(int);
   inline void __fastcall ChangeColor(int iB, int iG, int iR) 
      {iBlue = iB; iGreen = iG; iRed = iR; bColor_Change = true;}
};
//---------------------------------------------------------------------------
extern TForm1 *Form1;
//---------------------------------------------------------------------------
#endif
 