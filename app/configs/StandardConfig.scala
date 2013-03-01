package configs

import com.escalatesoft.subcut.inject.NewBindingModule
import org.squeryl.Session
import play.api.db.DB
import org.squeryl.adapters.H2Adapter
import play.api.Play.current

object StandardConfig extends NewBindingModule(module => {
  
  import module._
  
  bind [Session] toSingle Session.create(DB.getConnection(), new H2Adapter)

})
