/**
 \file $$ROOTFILENAME$$.cpp
 \brief Implements ...

 \author $$EXTENSIONAUTHORS$$ <$$EXTENSIONEMAILS$$>
 */

#include <openfluid/builderext/$$EXTENSIONTYPE$$.hpp>
#include <gtkmm/window.h>
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
  
    Gtk::Window* mp_Window;

  public:

    $$CLASSNAME$$()
    {
      mp_Window = new Gtk::Window();

      mp_Window->set_title("$$EXTENSIONSHORTNAME$$");
      mp_Window->set_default_size(640,480);
      mp_Window->set_modal(true);
      
      Gtk::VBox* MainBox = Gtk::manage(new Gtk::VBox());
      Gtk::Label* Label = Gtk::manage(new Gtk::Label("Auto-generated modal window"));      
      MainBox->pack_start(*Label);      
      MainBox->show_all_children();
      MainBox->set_visible(true);
      mp_Window->add(*MainBox);      
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
      return true; 
    }

    // =====================================================================
    // =====================================================================

    Gtk::Widget* getExtensionAsWidget()
    {
      return mp_Window;
    }

    // =====================================================================
    // =====================================================================

    void show()
    {
      mp_Window->show();
    }
    
};


// =====================================================================
// =====================================================================
$$PREFSPANELCLASSDEF$$

DEFINE_EXTENSION_HOOKS($$EXTENSIONHOOKS$$)


