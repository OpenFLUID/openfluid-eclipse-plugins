

#include <openfluid/guicommon/PreferencesManager.hpp>
#include <gtkmm/entry.h>
#include <gtkmm/label.h>
#include <gtkmm/box.h>


class $$CLASSNAME$$Prefs : public openfluid::builderext::BuilderExtensionPrefs
{
  private:
    
    // auto-generated sample code, remove it if necessary
    std::map<std::string,Gtk::Entry*> m_Entries;
    std::map<std::string,std::string> m_Labels;
    std::string m_ID;

  public:

    $$CLASSNAME$$Prefs()
      : openfluid::builderext::BuilderExtensionPrefs("$$EXTENSIONNAME$$"),
        m_ID("$$EXTENSIONID$$")
    {     
      $$PREFSENTRIESDECL$$

      Gtk::VBox* MainBox = Gtk::manage(new Gtk::VBox());

      for (std::map<std::string,Gtk::Entry*>::iterator it=m_Entries.begin();it!=m_Entries.end();++it)
      {
        Gtk::HBox* TmpBox = Gtk::manage(new Gtk::HBox());
        Gtk::Label* TmpLabel = Gtk::manage(new Gtk::Label(m_Labels[(*it).first] + ":"));
        (*it).second = Gtk::manage(new Gtk::Entry());
           (*it).second->signal_changed().connect(sigc::bind<std::string>(sigc::mem_fun(*this,&$$CLASSNAME$$Prefs::onEntryChanged),(*it).first));
        TmpBox->pack_start(*TmpLabel, Gtk::PACK_SHRINK,5);
        TmpBox->pack_start(*((*it).second), Gtk::PACK_SHRINK,5);
        MainBox->pack_start(*TmpBox, Gtk::PACK_SHRINK,15);
      }

      mp_ContentWindow->add(*MainBox);
      mp_ContentWindow->show_all_children();
    }

    // =====================================================================
    // =====================================================================

    ~$$CLASSNAME$$Prefs()
    {

    }

    // =====================================================================
    // =====================================================================

    void init()
    {      
       for (std::map<std::string,Gtk::Entry*>::iterator it=m_Entries.begin();it!=m_Entries.end();++it)
       {
         (*it).second->set_text(openfluid::guicommon::PreferencesManager::getInstance()->getPluginValue(m_ID,(*it).first));
       }
    }

    // =====================================================================
    // =====================================================================

    void onEntryChanged(std::string Param)
    {     
      openfluid::guicommon::PreferencesManager::getInstance()->setPluginValue(m_ID, Param, m_Entries[Param]->get_text());
    }

};


// =====================================================================
// =====================================================================
