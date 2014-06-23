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


/**

*/
class $$CLASSNAME$$ : public openfluid::base::PluggableFunction
{
  private:

  public:
    /**
      Constructor
    */
    $$CLASSNAME$$();

    /**
      Destructor
    */
    ~$$CLASSNAME$$();

    bool initParams(openfluid::core::FuncParamsMap_t Params);

    bool prepareData();

    bool checkConsistency();

    bool initializeRun(const openfluid::base::SimulationInfo* SimInfo);

    bool runStep(const openfluid::base::SimulationStatus* SimStatus);

    bool finalizeRun(const openfluid::base::SimulationInfo* SimInfo);

};


// =====================================================================
// =====================================================================


DEFINE_FUNCTION_HOOK($$CLASSNAME$$);


// =====================================================================
// =====================================================================


BEGIN_SIGNATURE_HOOK
  DECLARE_SIGNATURE_ID(("$$FUNCTIONID$$"));
  DECLARE_SIGNATURE_NAME(("$$FUNCTIONNAME$$"));
  DECLARE_SIGNATURE_DESCRIPTION(("$$FUNCTIONDESC$$"));

  DECLARE_SIGNATURE_VERSION(("1.0"));
  DECLARE_SIGNATURE_SDKVERSION;
  DECLARE_SIGNATURE_STATUS(openfluid::base::EXPERIMENTAL);

  DECLARE_SIGNATURE_DOMAIN(("$$FUNCTIONDOMAIN$$"));
  DECLARE_SIGNATURE_PROCESS(("$$FUNCTIONPROCESS$$"));
  DECLARE_SIGNATURE_METHOD(("$$FUNCTIONMETHOD$$"));
  DECLARE_SIGNATURE_AUTHORNAME(("$$FUNCTIONAUTHOR$$"));
  DECLARE_SIGNATURE_AUTHOREMAIL(("$$FUNCTIONAUTHOREMAIL$$"));

  $$FUNCTIONDECLARATION_PARAMS$$
  $$FUNCTIONDECLARATION_IDATA$$
  $$FUNCTIONDECLARATION_VARS$$
  $$FUNCTIONDECLARATION_EVENTS$$
  $$FUNCTIONDECLARATION_FILES$$

END_SIGNATURE_HOOK


// =====================================================================
// =====================================================================


$$CLASSNAME$$::$$CLASSNAME$$()
                : PluggableFunction()
{


}


// =====================================================================
// =====================================================================


$$CLASSNAME$$::~$$CLASSNAME$$()
{


}


// =====================================================================
// =====================================================================


bool $$CLASSNAME$$::initParams(openfluid::core::FuncParamsMap_t Params)
{


  return true;
}

// =====================================================================
// =====================================================================


bool $$CLASSNAME$$::prepareData()
{


  return true;
}


// =====================================================================
// =====================================================================


bool $$CLASSNAME$$::checkConsistency()
{


  return true;
}


// =====================================================================
// =====================================================================


bool $$CLASSNAME$$::initializeRun(const openfluid::base::SimulationInfo* SimInfo)
{


  return true;
}

// =====================================================================
// =====================================================================


bool $$CLASSNAME$$::runStep(const openfluid::base::SimulationStatus* SimStatus)
{

  return true;
}

// =====================================================================
// =====================================================================


bool $$CLASSNAME$$::finalizeRun(const openfluid::base::SimulationInfo* SimInfo)
{


  return true;
}

