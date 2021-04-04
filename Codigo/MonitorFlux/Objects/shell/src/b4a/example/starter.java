
package b4a.example;

import java.io.IOException;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.pc.PCBA;
import anywheresoftware.b4a.pc.RDebug;
import anywheresoftware.b4a.pc.RemoteObject;
import anywheresoftware.b4a.pc.RDebug.IRemote;
import anywheresoftware.b4a.pc.Debug;
import anywheresoftware.b4a.pc.B4XTypes.B4XClass;
import anywheresoftware.b4a.pc.B4XTypes.DeviceClass;

public class starter implements IRemote{
	public static starter mostCurrent;
	public static RemoteObject processBA;
    public static boolean processGlobalsRun;
    public static RemoteObject myClass;
    public static RemoteObject remoteMe;
	public starter() {
		mostCurrent = this;
	}
    public RemoteObject getRemoteMe() {
        return remoteMe;    
    }
    
public boolean isSingleton() {
		return true;
	}
    static {
        anywheresoftware.b4a.pc.RapidSub.moduleToObject.put(new B4XClass("starter"), "b4a.example.starter");
	}
     public static RemoteObject getObject() {
		return myClass;
	 }
	public RemoteObject _service;
    private PCBA pcBA;

	public PCBA create(Object[] args) throws ClassNotFoundException{
		processBA = (RemoteObject) args[1];
        _service = (RemoteObject) args[2];
        remoteMe = RemoteObject.declareNull("b4a.example.starter");
        anywheresoftware.b4a.keywords.Common.Density = (Float)args[3];
		pcBA = new PCBA(this, starter.class);
        main_subs_0.initializeProcessGlobals();
		return pcBA;
	}public static RemoteObject runMethod(boolean notUsed, String method, Object... args) throws Exception{
		return (RemoteObject) mostCurrent.pcBA.raiseEvent(method.substring(1), args);
	}
    public static void runVoidMethod(String method, Object... args) throws Exception{
		runMethod(false, method, args);
	}
public static RemoteObject __c = RemoteObject.declareNull("anywheresoftware.b4a.keywords.Common");
public static RemoteObject _sql = RemoteObject.declareNull("anywheresoftware.b4a.sql.SQL");
public static RemoteObject _rp = RemoteObject.declareNull("anywheresoftware.b4a.objects.RuntimePermissions");
public static RemoteObject _usuarioadmin = RemoteObject.createImmutable("");
public static RemoteObject _claveadmin = RemoteObject.createImmutable("");
public static RemoteObject _idadmin = RemoteObject.createImmutable("");
public static b4a.example.main _main = null;
public static b4a.example.tutoriales_activity _tutoriales_activity = null;
public static b4a.example.agregar_activity _agregar_activity = null;
public static b4a.example.monitor_activity _monitor_activity = null;
public static b4a.example.datos_activity _datos_activity = null;
public static b4a.example.registrar_activity _registrar_activity = null;
public static b4a.example.httputils2service _httputils2service = null;
public static b4a.example.dbutils _dbutils = null;
  public Object[] GetGlobals() {
		return new Object[] {"Agregar_Activity",Debug.moduleToString(b4a.example.agregar_activity.class),"claveAdmin",starter._claveadmin,"Datos_Activity",Debug.moduleToString(b4a.example.datos_activity.class),"DBUtils",Debug.moduleToString(b4a.example.dbutils.class),"HttpUtils2Service",Debug.moduleToString(b4a.example.httputils2service.class),"idAdmin",starter._idadmin,"Main",Debug.moduleToString(b4a.example.main.class),"Monitor_Activity",Debug.moduleToString(b4a.example.monitor_activity.class),"Registrar_Activity",Debug.moduleToString(b4a.example.registrar_activity.class),"rp",starter._rp,"Service",starter.mostCurrent._service,"sql",starter._sql,"Tutoriales_Activity",Debug.moduleToString(b4a.example.tutoriales_activity.class),"usuarioAdmin",starter._usuarioadmin};
}
}