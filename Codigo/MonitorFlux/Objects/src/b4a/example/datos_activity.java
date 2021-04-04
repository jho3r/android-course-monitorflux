package b4a.example;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class datos_activity extends Activity implements B4AActivity{
	public static datos_activity mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new anywheresoftware.b4a.ShellBA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.datos_activity");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (datos_activity).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.datos_activity");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.datos_activity", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (datos_activity) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (datos_activity) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return datos_activity.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (datos_activity) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (datos_activity) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            datos_activity mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (datos_activity) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }



public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public anywheresoftware.b4a.keywords.Common __c = null;
public b4a.example.httpjob _backendelessget = null;
public b4a.example.httpjob _historial = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbnombre = null;
public static String _urlget = "";
public static String _urlhistorial = "";
public anywheresoftware.b4a.objects.LabelWrapper _lbnumero = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbdescrip = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbestado = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbflujo = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbactualizado = null;
public static String _idactual = "";
public b4a.example.main _main = null;
public b4a.example.starter _starter = null;
public b4a.example.tutoriales_activity _tutoriales_activity = null;
public b4a.example.agregar_activity _agregar_activity = null;
public b4a.example.monitor_activity _monitor_activity = null;
public b4a.example.registrar_activity _registrar_activity = null;
public b4a.example.httputils2service _httputils2service = null;
public b4a.example.dbutils _dbutils = null;
public static String  _activity_create(boolean _firsttime) throws Exception{
RDebugUtils.currentModule="datos_activity";
if (Debug.shouldDelegate(mostCurrent.activityBA, "activity_create", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "activity_create", new Object[] {_firsttime}));}
RDebugUtils.currentLine=6684672;
 //BA.debugLineNum = 6684672;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
RDebugUtils.currentLine=6684675;
 //BA.debugLineNum = 6684675;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
RDebugUtils.currentLine=6684676;
 //BA.debugLineNum = 6684676;BA.debugLine="Activity.LoadLayout(\"Datos\")";
mostCurrent._activity.LoadLayout("Datos",mostCurrent.activityBA);
RDebugUtils.currentLine=6684677;
 //BA.debugLineNum = 6684677;BA.debugLine="SetStatusBarColor(Colors.RGB(231,231,222))";
_setstatusbarcolor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (231),(int) (231),(int) (222)));
RDebugUtils.currentLine=6684678;
 //BA.debugLineNum = 6684678;BA.debugLine="urlGet = \"https://api.backendless.com/4D75900B-E5";
mostCurrent._urlget = "https://api.backendless.com/4D75900B-E59C-1318-FF7D-6D0FBCB48400/A5201E9F-9465-4336-B56B-C606DDD986ED/data/Dispositivos?where=ownerId%20%3D%20";
RDebugUtils.currentLine=6684679;
 //BA.debugLineNum = 6684679;BA.debugLine="urlHistorial = Main.urlHistorial & \"&property=flu";
mostCurrent._urlhistorial = mostCurrent._main._urlhistorial /*String*/ +"&property=flujo";
RDebugUtils.currentLine=6684680;
 //BA.debugLineNum = 6684680;BA.debugLine="backendelessGet.Initialize(\"get\",Me)";
mostCurrent._backendelessget._initialize /*String*/ (null,processBA,"get",datos_activity.getObject());
RDebugUtils.currentLine=6684681;
 //BA.debugLineNum = 6684681;BA.debugLine="historial.Initialize(\"historial\",Me)";
mostCurrent._historial._initialize /*String*/ (null,processBA,"historial",datos_activity.getObject());
RDebugUtils.currentLine=6684682;
 //BA.debugLineNum = 6684682;BA.debugLine="backendelessGet.Download(urlGet & \"'\" & Main.ID &";
mostCurrent._backendelessget._download /*String*/ (null,mostCurrent._urlget+"'"+mostCurrent._main._id /*String*/ +"'");
RDebugUtils.currentLine=6684683;
 //BA.debugLineNum = 6684683;BA.debugLine="End Sub";
return "";
}
public static String  _setstatusbarcolor(int _clr) throws Exception{
RDebugUtils.currentModule="datos_activity";
if (Debug.shouldDelegate(mostCurrent.activityBA, "setstatusbarcolor", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "setstatusbarcolor", new Object[] {_clr}));}
anywheresoftware.b4a.phone.Phone _p = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4j.object.JavaObject _window = null;
RDebugUtils.currentLine=7208960;
 //BA.debugLineNum = 7208960;BA.debugLine="Sub SetStatusBarColor(clr As Int)";
RDebugUtils.currentLine=7208961;
 //BA.debugLineNum = 7208961;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
RDebugUtils.currentLine=7208962;
 //BA.debugLineNum = 7208962;BA.debugLine="If p.SdkVersion >= 21 Then";
if (_p.getSdkVersion()>=21) { 
RDebugUtils.currentLine=7208963;
 //BA.debugLineNum = 7208963;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
RDebugUtils.currentLine=7208964;
 //BA.debugLineNum = 7208964;BA.debugLine="jo.InitializeContext";
_jo.InitializeContext(processBA);
RDebugUtils.currentLine=7208965;
 //BA.debugLineNum = 7208965;BA.debugLine="Dim window As JavaObject = jo.RunMethodJO(\"getWi";
_window = new anywheresoftware.b4j.object.JavaObject();
_window = _jo.RunMethodJO("getWindow",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
RDebugUtils.currentLine=7208966;
 //BA.debugLineNum = 7208966;BA.debugLine="window.RunMethod(\"addFlags\", Array (0x80000000))";
_window.RunMethod("addFlags",new Object[]{(Object)(0x80000000)});
RDebugUtils.currentLine=7208967;
 //BA.debugLineNum = 7208967;BA.debugLine="window.RunMethod(\"clearFlags\", Array (0x04000000";
_window.RunMethod("clearFlags",new Object[]{(Object)(0x04000000)});
RDebugUtils.currentLine=7208968;
 //BA.debugLineNum = 7208968;BA.debugLine="window.RunMethod(\"setStatusBarColor\", Array(clr)";
_window.RunMethod("setStatusBarColor",new Object[]{(Object)(_clr)});
 };
RDebugUtils.currentLine=7208970;
 //BA.debugLineNum = 7208970;BA.debugLine="If p.SdkVersion >= 23 Then";
if (_p.getSdkVersion()>=23) { 
RDebugUtils.currentLine=7208971;
 //BA.debugLineNum = 7208971;BA.debugLine="jo = Activity";
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(mostCurrent._activity.getObject()));
RDebugUtils.currentLine=7208972;
 //BA.debugLineNum = 7208972;BA.debugLine="jo.RunMethod(\"setSystemUiVisibility\", Array(8192";
_jo.RunMethod("setSystemUiVisibility",new Object[]{(Object)(8192)});
 };
RDebugUtils.currentLine=7208974;
 //BA.debugLineNum = 7208974;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
RDebugUtils.currentModule="datos_activity";
RDebugUtils.currentLine=6815744;
 //BA.debugLineNum = 6815744;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
RDebugUtils.currentLine=6815746;
 //BA.debugLineNum = 6815746;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
RDebugUtils.currentModule="datos_activity";
if (Debug.shouldDelegate(mostCurrent.activityBA, "activity_resume", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "activity_resume", null));}
RDebugUtils.currentLine=6750208;
 //BA.debugLineNum = 6750208;BA.debugLine="Sub Activity_Resume";
RDebugUtils.currentLine=6750210;
 //BA.debugLineNum = 6750210;BA.debugLine="End Sub";
return "";
}
public static String  _btnatrase_click() throws Exception{
RDebugUtils.currentModule="datos_activity";
if (Debug.shouldDelegate(mostCurrent.activityBA, "btnatrase_click", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "btnatrase_click", null));}
RDebugUtils.currentLine=6881280;
 //BA.debugLineNum = 6881280;BA.debugLine="Sub btnAtrasE_Click";
RDebugUtils.currentLine=6881281;
 //BA.debugLineNum = 6881281;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
RDebugUtils.currentLine=6881282;
 //BA.debugLineNum = 6881282;BA.debugLine="End Sub";
return "";
}
public static String  _cargardatos(String _res) throws Exception{
RDebugUtils.currentModule="datos_activity";
if (Debug.shouldDelegate(mostCurrent.activityBA, "cargardatos", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "cargardatos", new Object[] {_res}));}
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.List _root = null;
anywheresoftware.b4a.objects.collections.Map _colroot = null;
String _nombre = "";
String _descripcion = "";
String _numero = "";
RDebugUtils.currentLine=7012352;
 //BA.debugLineNum = 7012352;BA.debugLine="Sub cargarDatos (res As String)";
RDebugUtils.currentLine=7012353;
 //BA.debugLineNum = 7012353;BA.debugLine="Dim parser As JSONParser 						'definimos objeto";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
RDebugUtils.currentLine=7012354;
 //BA.debugLineNum = 7012354;BA.debugLine="parser.Initialize(res)";
_parser.Initialize(_res);
RDebugUtils.currentLine=7012357;
 //BA.debugLineNum = 7012357;BA.debugLine="Dim root As List = parser.NextArray";
_root = new anywheresoftware.b4a.objects.collections.List();
_root = _parser.NextArray();
RDebugUtils.currentLine=7012358;
 //BA.debugLineNum = 7012358;BA.debugLine="For Each colroot As Map In root				'map es simila";
_colroot = new anywheresoftware.b4a.objects.collections.Map();
{
final anywheresoftware.b4a.BA.IterableList group4 = _root;
final int groupLen4 = group4.getSize()
;int index4 = 0;
;
for (; index4 < groupLen4;index4++){
_colroot = (anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (anywheresoftware.b4a.objects.collections.Map.MyMap)(group4.Get(index4)));
RDebugUtils.currentLine=7012359;
 //BA.debugLineNum = 7012359;BA.debugLine="If colroot.Get(\"nombre\") = Monitor_Activity.nomb";
if ((_colroot.Get((Object)("nombre"))).equals((Object)(mostCurrent._monitor_activity._nombred /*String*/ ))) { 
RDebugUtils.currentLine=7012360;
 //BA.debugLineNum = 7012360;BA.debugLine="Dim nombre As String = colroot.Get(\"nombre\")";
_nombre = BA.ObjectToString(_colroot.Get((Object)("nombre")));
RDebugUtils.currentLine=7012361;
 //BA.debugLineNum = 7012361;BA.debugLine="Dim descripcion As String = colroot.Get(\"descri";
_descripcion = BA.ObjectToString(_colroot.Get((Object)("descripcion")));
RDebugUtils.currentLine=7012362;
 //BA.debugLineNum = 7012362;BA.debugLine="Dim numero As String = colroot.Get(\"numero\")";
_numero = BA.ObjectToString(_colroot.Get((Object)("numero")));
RDebugUtils.currentLine=7012363;
 //BA.debugLineNum = 7012363;BA.debugLine="idActual = colroot.Get(\"id\")";
mostCurrent._idactual = BA.ObjectToString(_colroot.Get((Object)("id")));
 };
 }
};
RDebugUtils.currentLine=7012367;
 //BA.debugLineNum = 7012367;BA.debugLine="lbNombre.Text = nombre";
mostCurrent._lbnombre.setText(BA.ObjectToCharSequence(_nombre));
RDebugUtils.currentLine=7012368;
 //BA.debugLineNum = 7012368;BA.debugLine="lbNumero.Text = numero";
mostCurrent._lbnumero.setText(BA.ObjectToCharSequence(_numero));
RDebugUtils.currentLine=7012369;
 //BA.debugLineNum = 7012369;BA.debugLine="lbDescrip.Text = descripcion";
mostCurrent._lbdescrip.setText(BA.ObjectToCharSequence(_descripcion));
RDebugUtils.currentLine=7012370;
 //BA.debugLineNum = 7012370;BA.debugLine="historial.Download(urlHistorial)";
mostCurrent._historial._download /*String*/ (null,mostCurrent._urlhistorial);
RDebugUtils.currentLine=7012371;
 //BA.debugLineNum = 7012371;BA.debugLine="End Sub";
return "";
}
public static String  _cargarestado(String _res) throws Exception{
RDebugUtils.currentModule="datos_activity";
if (Debug.shouldDelegate(mostCurrent.activityBA, "cargarestado", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "cargarestado", new Object[] {_res}));}
long _fecha = 0L;
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.List _root = null;
anywheresoftware.b4a.objects.collections.Map _colroot = null;
long _fechaentra = 0L;
String _estado = "";
String _flujo = "";
RDebugUtils.currentLine=7077888;
 //BA.debugLineNum = 7077888;BA.debugLine="Sub cargarEstado (res As String)";
RDebugUtils.currentLine=7077889;
 //BA.debugLineNum = 7077889;BA.debugLine="Dim fecha As Long = 0";
_fecha = (long) (0);
RDebugUtils.currentLine=7077890;
 //BA.debugLineNum = 7077890;BA.debugLine="Dim parser As JSONParser 						'definimos objeto";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
RDebugUtils.currentLine=7077891;
 //BA.debugLineNum = 7077891;BA.debugLine="Log(res)";
anywheresoftware.b4a.keywords.Common.LogImpl("17077891",_res,0);
RDebugUtils.currentLine=7077892;
 //BA.debugLineNum = 7077892;BA.debugLine="parser.Initialize(res)";
_parser.Initialize(_res);
RDebugUtils.currentLine=7077895;
 //BA.debugLineNum = 7077895;BA.debugLine="Dim root As List = parser.NextArray";
_root = new anywheresoftware.b4a.objects.collections.List();
_root = _parser.NextArray();
RDebugUtils.currentLine=7077896;
 //BA.debugLineNum = 7077896;BA.debugLine="For Each colroot As Map In root				'map es simila";
_colroot = new anywheresoftware.b4a.objects.collections.Map();
{
final anywheresoftware.b4a.BA.IterableList group6 = _root;
final int groupLen6 = group6.getSize()
;int index6 = 0;
;
for (; index6 < groupLen6;index6++){
_colroot = (anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (anywheresoftware.b4a.objects.collections.Map.MyMap)(group6.Get(index6)));
RDebugUtils.currentLine=7077898;
 //BA.debugLineNum = 7077898;BA.debugLine="If colroot.Get(\"id\") = idActual Then";
if ((_colroot.Get((Object)("id"))).equals((Object)(mostCurrent._idactual))) { 
RDebugUtils.currentLine=7077900;
 //BA.debugLineNum = 7077900;BA.debugLine="Dim fechaEntra As Long = colroot.Get(\"fecha\")";
_fechaentra = BA.ObjectToLongNumber(_colroot.Get((Object)("fecha")));
RDebugUtils.currentLine=7077901;
 //BA.debugLineNum = 7077901;BA.debugLine="If fechaEntra >= fecha Then";
if (_fechaentra>=_fecha) { 
RDebugUtils.currentLine=7077902;
 //BA.debugLineNum = 7077902;BA.debugLine="Dim estado As String = colroot.Get(\"encendida\"";
_estado = BA.ObjectToString(_colroot.Get((Object)("encendida")));
RDebugUtils.currentLine=7077903;
 //BA.debugLineNum = 7077903;BA.debugLine="Dim flujo As String = colroot.Get(\"flujo\")";
_flujo = BA.ObjectToString(_colroot.Get((Object)("flujo")));
RDebugUtils.currentLine=7077904;
 //BA.debugLineNum = 7077904;BA.debugLine="Log(colroot.Get(\"flujo\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("17077904",BA.ObjectToString(_colroot.Get((Object)("flujo"))),0);
RDebugUtils.currentLine=7077905;
 //BA.debugLineNum = 7077905;BA.debugLine="fecha = fechaEntra";
_fecha = _fechaentra;
 };
 };
 }
};
RDebugUtils.currentLine=7077910;
 //BA.debugLineNum = 7077910;BA.debugLine="If estado = True Then";
if ((_estado).equals(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True))) { 
RDebugUtils.currentLine=7077911;
 //BA.debugLineNum = 7077911;BA.debugLine="lbEstado.Text = \"Encendido\"";
mostCurrent._lbestado.setText(BA.ObjectToCharSequence("Encendido"));
RDebugUtils.currentLine=7077912;
 //BA.debugLineNum = 7077912;BA.debugLine="lbEstado.Color = Colors.ARGB(128,0,136,145)";
mostCurrent._lbestado.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (128),(int) (0),(int) (136),(int) (145)));
 }else 
{RDebugUtils.currentLine=7077913;
 //BA.debugLineNum = 7077913;BA.debugLine="Else If estado = False Then";
if ((_estado).equals(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.False))) { 
RDebugUtils.currentLine=7077914;
 //BA.debugLineNum = 7077914;BA.debugLine="lbEstado.Text = \"Apagado\"";
mostCurrent._lbestado.setText(BA.ObjectToCharSequence("Apagado"));
RDebugUtils.currentLine=7077915;
 //BA.debugLineNum = 7077915;BA.debugLine="lbEstado.Color = Colors.ARGB(128,240,84,84)";
mostCurrent._lbestado.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (128),(int) (240),(int) (84),(int) (84)));
 }}
;
RDebugUtils.currentLine=7077919;
 //BA.debugLineNum = 7077919;BA.debugLine="lbFlujo.Text = flujo & \" Litros/Hora\"";
mostCurrent._lbflujo.setText(BA.ObjectToCharSequence(_flujo+" Litros/Hora"));
RDebugUtils.currentLine=7077922;
 //BA.debugLineNum = 7077922;BA.debugLine="diferenciaDeFechas(fecha)";
_diferenciadefechas(_fecha);
RDebugUtils.currentLine=7077923;
 //BA.debugLineNum = 7077923;BA.debugLine="End Sub";
return "";
}
public static String  _diferenciadefechas(long _fecha) throws Exception{
RDebugUtils.currentModule="datos_activity";
if (Debug.shouldDelegate(mostCurrent.activityBA, "diferenciadefechas", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "diferenciadefechas", new Object[] {_fecha}));}
long _fechaactual = 0L;
long _actual = 0L;
long _actualmodificado = 0L;
String _mensaje = "";
RDebugUtils.currentLine=7143424;
 //BA.debugLineNum = 7143424;BA.debugLine="Sub diferenciaDeFechas(fecha As Long){";
RDebugUtils.currentLine=7143426;
 //BA.debugLineNum = 7143426;BA.debugLine="DateTime.DateFormat = \"yyyyMMddHHmm\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyyMMddHHmm");
RDebugUtils.currentLine=7143427;
 //BA.debugLineNum = 7143427;BA.debugLine="Dim fechaActual As Long = DateTime.Date(DateTime.";
_fechaactual = (long)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())));
RDebugUtils.currentLine=7143428;
 //BA.debugLineNum = 7143428;BA.debugLine="Dim actual As Long = fechaActual - fecha";
_actual = (long) (_fechaactual-_fecha);
RDebugUtils.currentLine=7143429;
 //BA.debugLineNum = 7143429;BA.debugLine="Dim actualModificado As Long";
_actualmodificado = 0L;
RDebugUtils.currentLine=7143430;
 //BA.debugLineNum = 7143430;BA.debugLine="Dim mensaje As String";
_mensaje = "";
RDebugUtils.currentLine=7143432;
 //BA.debugLineNum = 7143432;BA.debugLine="If actual < 100 Then";
if (_actual<100) { 
RDebugUtils.currentLine=7143433;
 //BA.debugLineNum = 7143433;BA.debugLine="actualModificado = fechaActual/100";
_actualmodificado = (long) (_fechaactual/(double)100);
RDebugUtils.currentLine=7143434;
 //BA.debugLineNum = 7143434;BA.debugLine="fechaActual = fechaActual - actualModificado*100";
_fechaactual = (long) (_fechaactual-_actualmodificado*100);
RDebugUtils.currentLine=7143435;
 //BA.debugLineNum = 7143435;BA.debugLine="actualModificado = fecha/100";
_actualmodificado = (long) (_fecha/(double)100);
RDebugUtils.currentLine=7143436;
 //BA.debugLineNum = 7143436;BA.debugLine="fecha = fecha - actualModificado*100";
_fecha = (long) (_fecha-_actualmodificado*100);
RDebugUtils.currentLine=7143437;
 //BA.debugLineNum = 7143437;BA.debugLine="If fecha < fechaActual Then";
if (_fecha<_fechaactual) { 
RDebugUtils.currentLine=7143438;
 //BA.debugLineNum = 7143438;BA.debugLine="actualModificado = fechaActual - fecha";
_actualmodificado = (long) (_fechaactual-_fecha);
 }else {
RDebugUtils.currentLine=7143440;
 //BA.debugLineNum = 7143440;BA.debugLine="actualModificado = fechaActual + (60 - fecha)";
_actualmodificado = (long) (_fechaactual+(60-_fecha));
 };
RDebugUtils.currentLine=7143443;
 //BA.debugLineNum = 7143443;BA.debugLine="mensaje = \"Actualizado hace \" & actualModificado";
_mensaje = "Actualizado hace "+BA.NumberToString(_actualmodificado)+" minutos";
 }else 
{RDebugUtils.currentLine=7143445;
 //BA.debugLineNum = 7143445;BA.debugLine="Else If actual < 10000 Then";
if (_actual<10000) { 
RDebugUtils.currentLine=7143446;
 //BA.debugLineNum = 7143446;BA.debugLine="actualModificado = fechaActual/10000";
_actualmodificado = (long) (_fechaactual/(double)10000);
RDebugUtils.currentLine=7143447;
 //BA.debugLineNum = 7143447;BA.debugLine="fechaActual = fechaActual - actualModificado*100";
_fechaactual = (long) (_fechaactual-_actualmodificado*10000);
RDebugUtils.currentLine=7143448;
 //BA.debugLineNum = 7143448;BA.debugLine="actualModificado = fecha/10000";
_actualmodificado = (long) (_fecha/(double)10000);
RDebugUtils.currentLine=7143449;
 //BA.debugLineNum = 7143449;BA.debugLine="fecha = fecha - actualModificado*10000";
_fecha = (long) (_fecha-_actualmodificado*10000);
RDebugUtils.currentLine=7143450;
 //BA.debugLineNum = 7143450;BA.debugLine="Log(fechaActual)";
anywheresoftware.b4a.keywords.Common.LogImpl("17143450",BA.NumberToString(_fechaactual),0);
RDebugUtils.currentLine=7143451;
 //BA.debugLineNum = 7143451;BA.debugLine="Log(fecha)";
anywheresoftware.b4a.keywords.Common.LogImpl("17143451",BA.NumberToString(_fecha),0);
RDebugUtils.currentLine=7143452;
 //BA.debugLineNum = 7143452;BA.debugLine="If fecha < fechaActual Then";
if (_fecha<_fechaactual) { 
RDebugUtils.currentLine=7143453;
 //BA.debugLineNum = 7143453;BA.debugLine="actualModificado = fechaActual/100 - fecha/100";
_actualmodificado = (long) (_fechaactual/(double)100-_fecha/(double)100);
 }else {
RDebugUtils.currentLine=7143455;
 //BA.debugLineNum = 7143455;BA.debugLine="actualModificado = fechaActual/100 + (24 - fech";
_actualmodificado = (long) (_fechaactual/(double)100+(24-_fecha/(double)100));
 };
RDebugUtils.currentLine=7143458;
 //BA.debugLineNum = 7143458;BA.debugLine="mensaje = \"Actualizado hace \" & actualModificado";
_mensaje = "Actualizado hace "+BA.NumberToString(_actualmodificado)+" horas";
 }else 
{RDebugUtils.currentLine=7143461;
 //BA.debugLineNum = 7143461;BA.debugLine="Else If actual < 1000000 Then";
if (_actual<1000000) { 
RDebugUtils.currentLine=7143462;
 //BA.debugLineNum = 7143462;BA.debugLine="actualModificado = fechaActual/1000000";
_actualmodificado = (long) (_fechaactual/(double)1000000);
RDebugUtils.currentLine=7143463;
 //BA.debugLineNum = 7143463;BA.debugLine="fechaActual = fechaActual - actualModificado*100";
_fechaactual = (long) (_fechaactual-_actualmodificado*1000000);
RDebugUtils.currentLine=7143464;
 //BA.debugLineNum = 7143464;BA.debugLine="actualModificado = fecha/1000000";
_actualmodificado = (long) (_fecha/(double)1000000);
RDebugUtils.currentLine=7143465;
 //BA.debugLineNum = 7143465;BA.debugLine="fecha = fecha - actualModificado*1000000";
_fecha = (long) (_fecha-_actualmodificado*1000000);
RDebugUtils.currentLine=7143466;
 //BA.debugLineNum = 7143466;BA.debugLine="actualModificado = Abs(fechaActual - fecha)";
_actualmodificado = (long) (anywheresoftware.b4a.keywords.Common.Abs(_fechaactual-_fecha));
RDebugUtils.currentLine=7143467;
 //BA.debugLineNum = 7143467;BA.debugLine="If fecha < fechaActual Then";
if (_fecha<_fechaactual) { 
RDebugUtils.currentLine=7143468;
 //BA.debugLineNum = 7143468;BA.debugLine="actualModificado = fechaActual/10000 - fecha/10";
_actualmodificado = (long) (_fechaactual/(double)10000-_fecha/(double)10000);
 }else {
RDebugUtils.currentLine=7143470;
 //BA.debugLineNum = 7143470;BA.debugLine="actualModificado = fechaActual/10000 + (30 - fe";
_actualmodificado = (long) (_fechaactual/(double)10000+(30-_fecha/(double)10000));
 };
RDebugUtils.currentLine=7143473;
 //BA.debugLineNum = 7143473;BA.debugLine="mensaje = \"Actualizado hace \" & actualModificado";
_mensaje = "Actualizado hace "+BA.NumberToString(_actualmodificado)+" dias";
 }else 
{RDebugUtils.currentLine=7143476;
 //BA.debugLineNum = 7143476;BA.debugLine="Else If actual < 100000000 Then";
if (_actual<100000000) { 
RDebugUtils.currentLine=7143477;
 //BA.debugLineNum = 7143477;BA.debugLine="actualModificado = fechaActual/100000000";
_actualmodificado = (long) (_fechaactual/(double)100000000);
RDebugUtils.currentLine=7143478;
 //BA.debugLineNum = 7143478;BA.debugLine="fechaActual = fechaActual - actualModificado*100";
_fechaactual = (long) (_fechaactual-_actualmodificado*100000000);
RDebugUtils.currentLine=7143479;
 //BA.debugLineNum = 7143479;BA.debugLine="actualModificado = fecha/100000000";
_actualmodificado = (long) (_fecha/(double)100000000);
RDebugUtils.currentLine=7143480;
 //BA.debugLineNum = 7143480;BA.debugLine="fecha = fecha - actualModificado*100000000";
_fecha = (long) (_fecha-_actualmodificado*100000000);
RDebugUtils.currentLine=7143481;
 //BA.debugLineNum = 7143481;BA.debugLine="actualModificado = Abs(fechaActual - fecha)";
_actualmodificado = (long) (anywheresoftware.b4a.keywords.Common.Abs(_fechaactual-_fecha));
RDebugUtils.currentLine=7143482;
 //BA.debugLineNum = 7143482;BA.debugLine="If fecha < fechaActual Then";
if (_fecha<_fechaactual) { 
RDebugUtils.currentLine=7143483;
 //BA.debugLineNum = 7143483;BA.debugLine="actualModificado = fechaActual/1000000 - fecha/";
_actualmodificado = (long) (_fechaactual/(double)1000000-_fecha/(double)1000000);
 }else {
RDebugUtils.currentLine=7143485;
 //BA.debugLineNum = 7143485;BA.debugLine="actualModificado = fechaActual/1000000 + (12 -";
_actualmodificado = (long) (_fechaactual/(double)1000000+(12-_fecha/(double)1000000));
 };
RDebugUtils.currentLine=7143488;
 //BA.debugLineNum = 7143488;BA.debugLine="mensaje = \"Actualizado hace \" & actualModificado";
_mensaje = "Actualizado hace "+BA.NumberToString(_actualmodificado)+" meses";
 }else {
RDebugUtils.currentLine=7143491;
 //BA.debugLineNum = 7143491;BA.debugLine="mensaje = \"Desactualizado\"";
_mensaje = "Desactualizado";
 }}}}
;
RDebugUtils.currentLine=7143493;
 //BA.debugLineNum = 7143493;BA.debugLine="lbActualizado.Text = mensaje";
mostCurrent._lbactualizado.setText(BA.ObjectToCharSequence(_mensaje));
RDebugUtils.currentLine=7143494;
 //BA.debugLineNum = 7143494;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(b4a.example.httpjob _job) throws Exception{
RDebugUtils.currentModule="datos_activity";
if (Debug.shouldDelegate(mostCurrent.activityBA, "jobdone", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "jobdone", new Object[] {_job}));}
RDebugUtils.currentLine=6946816;
 //BA.debugLineNum = 6946816;BA.debugLine="Sub JobDone (Job As HttpJob)";
RDebugUtils.currentLine=6946817;
 //BA.debugLineNum = 6946817;BA.debugLine="Log(\"JobName = \" & Job.JobName & \", Success = \" &";
anywheresoftware.b4a.keywords.Common.LogImpl("16946817","JobName = "+_job._jobname /*String*/ +", Success = "+BA.ObjectToString(_job._success /*boolean*/ ),0);
RDebugUtils.currentLine=6946818;
 //BA.debugLineNum = 6946818;BA.debugLine="If Job.Success = True Then";
if (_job._success /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
RDebugUtils.currentLine=6946819;
 //BA.debugLineNum = 6946819;BA.debugLine="Select Job.JobName 'Nombre del proceso a traves";
switch (BA.switchObjectToInt(_job._jobname /*String*/ ,"get","historial")) {
case 0: {
RDebugUtils.currentLine=6946821;
 //BA.debugLineNum = 6946821;BA.debugLine="cargarDatos(Job.GetString) 'se envia la cadena";
_cargardatos(_job._getstring /*String*/ (null));
 break; }
case 1: {
RDebugUtils.currentLine=6946823;
 //BA.debugLineNum = 6946823;BA.debugLine="cargarEstado(Job.GetString)";
_cargarestado(_job._getstring /*String*/ (null));
 break; }
}
;
 }else {
RDebugUtils.currentLine=6946826;
 //BA.debugLineNum = 6946826;BA.debugLine="Log(\"Error: \" & Job.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("16946826","Error: "+_job._errormessage /*String*/ ,0);
RDebugUtils.currentLine=6946827;
 //BA.debugLineNum = 6946827;BA.debugLine="ToastMessageShow(\"Error: \" & Job.ErrorMessage, T";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error: "+_job._errormessage /*String*/ ),anywheresoftware.b4a.keywords.Common.True);
 };
RDebugUtils.currentLine=6946829;
 //BA.debugLineNum = 6946829;BA.debugLine="Job.Release";
_job._release /*String*/ (null);
RDebugUtils.currentLine=6946830;
 //BA.debugLineNum = 6946830;BA.debugLine="End Sub";
return "";
}
}