/**
  \file $$ROOTFILENAME$$.cpp
*/


/*
<sim2doc>

</sim2doc>
*/


#include <openfluid/ware/PluggableSimulator.hpp>


DECLARE_SIMULATOR_PLUGIN


// =====================================================================
// =====================================================================


BEGIN_SIMULATOR_SIGNATURE("$$SIMULATORID$$")

  DECLARE_NAME("$$SIMULATORNAME$$");
  DECLARE_DESCRIPTION("$$SIMULATORDESC$$");

  DECLARE_VERSION("$$SIMULATORVERSION$$");
  DECLARE_STATUS(openfluid::ware::EXPERIMENTAL);

  DECLARE_DOMAIN("$$SIMULATORDOMAIN$$");
  DECLARE_PROCESS("$$SIMULATORPROCESS$$");
  DECLARE_METHOD("$$SIMULATORMETHOD$$");
  DECLARE_AUTHOR("$$SIMULATORAUTHOR$$","$$SIMULATORAUTHOREMAIL$$");

  $$SIMULATORDECLARATION_PARAMS$$  
  $$SIMULATORDECLARATION_ATTRS$$
  $$SIMULATORDECLARATION_VARS$$
  $$SIMULATORDECLARATION_EVENTS$$
  $$SIMULATORDECLARATION_FILES$$
  $$SIMULATORDECLARATION_SCHED$$
  $$SIMULATORDECLARATION_SDYN$$

END_SIMULATOR_SIGNATURE


// =====================================================================
// =====================================================================


/**

*/
class $$CLASSNAME$$ : public openfluid::ware::PluggableSimulator
{
  private:

  
  public:

  
    $$CLASSNAME$$(): PluggableSimulator()
    {
  
  
    }
  
  
    // =====================================================================
    // =====================================================================
  
  
    ~$$CLASSNAME$$()
    {
  
  
    }
  
  
    // =====================================================================
    // =====================================================================
  
  
    void initParams(const openfluid::ware::WareParams_t& /*Params*/)
    {


    }


    // =====================================================================
    // =====================================================================
  
  
    void prepareData()
    {
  
  
    }
  
  
    // =====================================================================
    // =====================================================================
  
  
    void checkConsistency()
    {
  
  
    }
  
  
    // =====================================================================
    // =====================================================================
  
  
    openfluid::base::SchedulingRequest initializeRun()
    {  
    
      
      return DefaultDeltaT();
    }


    // =====================================================================
    // =====================================================================
  
  
    openfluid::base::SchedulingRequest runStep()
    {


      return DefaultDeltaT();
    }


    // =====================================================================
    // =====================================================================
  
  
    void finalizeRun()
    {
  
  
    }

};


// =====================================================================
// =====================================================================


DEFINE_SIMULATOR_CLASS($$CLASSNAME$$);

