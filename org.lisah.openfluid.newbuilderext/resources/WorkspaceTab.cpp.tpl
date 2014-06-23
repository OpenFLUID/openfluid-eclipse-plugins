/**
 \file $$ROOTFILENAME$$.cpp
 \brief Implements ...

 \author $$EXTENSIONAUTHORS$$ <$$EXTENSIONEMAILS$$>
 */

#include <openfluid/builderext/$$EXTENSIONTYPE$$.hpp>
#include <gtkmm/label.h>
#include <gtkmm/box.h>

DECLARE_EXTENSION_HOOKS

DEFINE_EXTENSION_INFOS("$$EXTENSIONID$$",
    "$$EXTENSIONSHORTNAME$$",
    "$$EXTENSIONNAME$$",
    "$$EXTENSIONDESC$$",
    "$$EXTENSIONAUTHORS$$",
    "$$EXTENSIONEMAILS$$",
    openfluid::builderext::PluggableBuilderExtension::$$EXTENSIONTYPE$$)

DEFINE_EXTENSION_DEFAULT_CONFIG($$PREFSDEFAULTS$$)


// =====================================================================
// =====================================================================


class $$CLASSNAME$$: public openfluid::builderext::$$EXTENSIONTYPE$$
{
  private:
  
    Gtk::VBox* mp_MainBox;
    
  public:

    $$CLASSNAME$$()
    {      
      Gtk::Label* Label = Gtk::manage(new Gtk::Label("Auto-generated workspace tab"));
      
      mp_MainBox = Gtk::manage(new Gtk::VBox());
      mp_MainBox->pack_start(*Label);
      
      mp_MainBox->show_all_children();
      mp_MainBox->set_visible(true);
    }

    // =====================================================================
    // =====================================================================

    ~$$CLASSNAME$$()
    {

    }

    // =====================================================================
    // =====================================================================

    bool isReadyForShowtime() const 
    { 
      return (mp_SimulationBlob != NULL); 
    }

    // =====================================================================
    // =====================================================================

    Gtk::Widget* getExtensionAsWidget()
    {
      return mp_MainBox;
    }

    // =====================================================================
    // =====================================================================

    void update()
    {
      
    }
    
};


// =====================================================================
// =====================================================================
$$PREFSPANELCLASSDEF$$

DEFINE_EXTENSION_HOOKS($$EXTENSIONHOOKS$$)


