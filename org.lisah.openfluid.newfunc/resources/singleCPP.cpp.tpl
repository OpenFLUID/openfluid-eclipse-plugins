/**
  \file $$ROOTFILENAME$$.cpp
*/


#include "openfluid-base.h"
#include "openfluid-core.h"


// =====================================================================
// =====================================================================


DECLARE_PLUGIN_HOOKS;


// =====================================================================
// =====================================================================


BEGIN_SIGNATURE_HOOK
  DECLARE_SIGNATURE_ID("$$FUNCTIONID$$");
  DECLARE_SIGNATURE_NAME("$$FUNCTIONNAME$$");
  DECLARE_SIGNATURE_DESCRIPTION("$$FUNCTIONDESC$$");

  DECLARE_SIGNATURE_VERSION("1.0");
  DECLARE_SIGNATURE_SDKVERSION;
  DECLARE_SIGNATURE_STATUS(openfluid::base::EXPERIMENTAL);

  DECLARE_SIGNATURE_DOMAIN("$$FUNCTIONDOMAIN$$");
  DECLARE_SIGNATURE_PROCESS("$$FUNCTIONPROCESS$$");
  DECLARE_SIGNATURE_METHOD("$$FUNCTIONMETHOD$$");
  DECLARE_SIGNATURE_AUTHORNAME("$$FUNCTIONAUTHOR$$");
  DECLARE_SIGNATURE_AUTHOREMAIL("$$FUNCTIONAUTHOREMAIL$$");

  $$FUNCTIONDECLARATION_PARAMS$$
  $$FUNCTIONDECLARATION_IDATA$$
  $$FUNCTIONDECLARATION_VARS$$
  $$FUNCTIONDECLARATION_EVENTS$$
  $$FUNCTIONDECLARATION_FILES$$

END_SIGNATURE_HOOK


// =====================================================================
// =====================================================================


/**

*/
class $$CLASSNAME$$ : public openfluid::base::PluggableFunction
{
  private:

  
  public:

  
    $$CLASSNAME$$(): PluggableFunction()
    {
  
  
    }
  
  
    // =====================================================================
    // =====================================================================
  
  
    ~$$CLASSNAME$$()
    {
  
  
    }
  
  
    // =====================================================================
    // =====================================================================
  
  
    bool initParams(openfluid::core::FuncParamsMap_t Params)
    {
  
  
      return true;
    }
  
    // =====================================================================
    // =====================================================================
  
  
    bool prepareData()
    {
  
  
      return true;
    }
  
  
    // =====================================================================
    // =====================================================================
  
  
    bool checkConsistency()
    {
  
  
      return true;
    }
  
  
    // =====================================================================
    // =====================================================================
  
  
    bool initializeRun(const openfluid::base::SimulationInfo* SimInfo)
    {
  
  
      return true;
    }
  
    // =====================================================================
    // =====================================================================
  
  
    bool runStep(const openfluid::base::SimulationStatus* SimStatus)
    {
  
      return true;
    }
  
    // =====================================================================
    // =====================================================================
  
  
    bool finalizeRun(const openfluid::base::SimulationInfo* SimInfo)
    {
  
  
      return true;
    }

};


// =====================================================================
// =====================================================================


DEFINE_FUNCTION_HOOK($$CLASSNAME$$);

