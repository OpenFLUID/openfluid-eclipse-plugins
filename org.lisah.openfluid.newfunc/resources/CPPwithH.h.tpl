/**
  \file $$ROOTFILENAME$$.cpp
*/


#ifndef $$HEADERGUARD$$
#define $$HEADERGUARD$$


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

#endif
