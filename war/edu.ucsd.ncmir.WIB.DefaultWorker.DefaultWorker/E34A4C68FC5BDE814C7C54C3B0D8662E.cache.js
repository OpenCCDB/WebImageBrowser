function T(){}
function Sc(){}
function tc(){}
function wc(){}
function wb(){}
function ab(){}
function Hc(a,b){}
function vb(){kb()}
function B(){nb(kb())}
function Dc(){B.call(this)}
function Fc(){B.call(this)}
function Q(){Q=Sc;P=new T}
function kc(a){return new ic[a]}
function Kc(b,a){return b.indexOf(a)}
function Lc(b,a){return b.lastIndexOf(a)}
function pc(b,a){b.postMessage(a)}
function oc(a){a.b=$self;qc(a.b,a)}
function M(a){$wnd.clearTimeout(a)}
function X(a){return parseInt(a)||-1}
function Qc(a){return yb(ec,Vc,1,a,0)}
function Hb(a,b){return a!=null&&Fb(a,b)}
function Fb(a,b){return a.cM&&!!a.cM[b]}
function H(a,b,c){return a.apply(b,c);var d}
function Mc(c,a,b){return c.lastIndexOf(a,b)}
function Oc(b,a){return b.substr(a,b.length-a)}
function Ac(a){return typeof a=='number'&&a>0}
function zc(a){var b=ic[a.b];a=null;return b}
function U(a,b){!a&&(a=[]);a[a.length]=b;return a}
function Y(a,b){a.length>=b&&a.splice(0,b);return a}
function $(){try{null.a()}catch(a){return a}}
function nb(){var a;a=lb(new vb);pb(a)}
function Cb(){Cb=Sc;Ab=[];Bb=[];Db(new wb,Ab,Bb)}
function C(a){B.call(this);this.b=a;mb(new vb,this)}
function gc(a){if(Hb(a,2)){return a}return new C(a)}
function Jc(a,b){if(b==null){return false}return String(a)==b}
function qc(c,b){c.onmessage=function(a){b.i(a)}}
function kb(){kb=Sc;Error.stackTraceLimit=128}
function Ib(a){return a!=null&&a.tM!=Sc&&!Fb(a,1)}
function Gb(a){if(a!=null&&(a.tM==Sc||Fb(a,1))){throw new Dc}return a}
function yb(a,b,c,d,e){var f;f=xb(e,d);zb(a,b,c,f);return f}
function yc(a,b,c){var d;d=new wc;Ac(c)&&Bc(c,d);return d}
function xc(a,b,c){var d;d=new wc;Ac(c!=0?-c:0)&&Bc(c!=0?-c:0,d);return d}
function zb(a,b,c,d){Cb();Eb(d,Ab,Bb);d.cZ=a;d.cM=b;return d}
function mb(a,b){var c;c=ob(a,Ib(b.b)?Gb(b.b):null);pb(c)}
function K(a,b,c){var d;d=I();try{return H(a,b,c)}finally{L(d)}}
function ob(a,b){var c;c=gb(a,b);return c.length==0?(new ab).f(b):Y(c,1)}
function lb(a){var b;b=Y(ob(a,$()),3);b.length==0&&(b=Y((new ab).d(),1));return b}
function L(a){a&&S((Q(),P));--E;if(a){if(G!=-1){M(G);G=-1}}}
function N(){return $wnd.setTimeout(function(){E!=0&&(E=0);G=-1},10)}
function J(b){return function(){try{return K(b,this,arguments)}catch(a){throw a}}}
function Eb(a,b,c){Cb();for(var d=0,e=b.length;d<e;++d){a[b[d]]=c[d]}}
function Db(a,b,c){var d=0,e;for(var f in a){if(e=a[f]){b[d]=f;c[d]=e;++d}}}
function R(a){var b,c;if(a.b){c=null;do{b=a.b;a.b=null;c=V(b,c)}while(a.b);a.b=c}}
function S(a){var b,c;if(a.c){c=null;do{b=a.c;a.c=null;c=V(b,c)}while(a.c);a.c=c}}
function A(a){var b,c,d;c=yb(dc,Vc,0,a.length,0);for(d=0,b=a.length;d<b;++d){if(!a[d]){throw new Fc}c[d]=a[d]}}
function gb(a,b){var c,d,e;e=b&&b.stack?b.stack.split('\n'):[];for(c=0,d=e.length;c<d;++c){e[c]=a.e(e[c])}return e}
function gwtOnLoad(b,c,d,e){$moduleName=c;$moduleBase=d;if(b)try{Xc(fc)()}catch(a){b(c)}else{Xc(fc)()}}
function I(){var a;if(E!=0){a=(new Date).getTime();if(a-F>2000){F=a;G=N()}}if(E++==0){R((Q(),P));return true}return false}
function Pc(c){if(c.length==0||c[0]>cd&&c[c.length-1]>cd){return c}var a=c.replace(/^(\s*)/,Yc);var b=a.replace(/\s*$/,Yc);return b}
function V(b,c){var d,e,f;for(d=0,e=b.length;d<e;++d){f=b[d];try{f[1]?f[0].j()&&(c=U(c,f)):f[0].j()}catch(a){a=gc(a);if(!Hb(a,2))throw a}}return c}
function Bc(a,b){var c;b.b=a;if(a==2){c=String.prototype}else{if(a>0){var d=zc(b);if(d){c=d.prototype}else{d=ic[a]=function(){};d.cZ=b;return}}else{return}}c.cZ=b}
function xb(a,b){var c=new Array(b);if(a==3){for(var d=0;d<b;++d){var e=new Object;e.l=e.m=e.h=0;c[d]=e}}else if(a>0){var e=[null,0,false][a];for(var d=0;d<b;++d){c[d]=e}}return c}
function sc(a,b,c,d,e){var f,g,h;g=yb(bc,Vc,-1,256,1);if(a)for(f=0;f<256;++f){h=f*d+e;h=~~Math.max(Math.min(h*b+c,2147483647),-2147483648);h<0?(h=0):h>255&&(h=255);g[f]=h}return g}
function lc(a){return $stats({moduleName:$moduleName,sessionId:$sessionId,subSystem:'startup',evtGroup:'moduleStartup',millis:(new Date).getTime(),type:'onModuleLoadStart',className:a})}
function W(a){var b,c,d;d=Yc;a=Pc(a);b=a.indexOf(Zc);c=a.indexOf('function')==0?8:0;if(b==-1){b=Kc(a,Rc(64));c=a.indexOf('function ')==0?9:0}b!=-1&&(d=Pc(a.substr(c,b-c)));return d.length>0?d:$c}
function Rc(a){var b,c;if(a>=65536){b=55296+(~~(a-65536)>>10&1023)&65535;c=56320+(a-65536&1023)&65535;return String.fromCharCode(b)+String.fromCharCode(c)}else{return String.fromCharCode(a&65535)}}
function jc(a,b,c){var d=ic[a];if(d&&!d.cZ){_=d.prototype}else{!d&&(d=ic[a]=function(){});_=d.prototype=b<0?{}:kc(b);_.cM=c}for(var e=3;e<arguments.length;++e){arguments[e].prototype=_}if(d.cZ){_.cZ=d.cZ;d.cZ=null}}
function fc(){!!$stats&&lc('com.google.gwt.useragent.client.UserAgentAsserter');!!$stats&&lc('com.google.gwt.user.client.DocumentModeAsserter');mc();!!$stats&&lc('edu.ucsd.ncmir.WIB.DefaultWorker.worker.DefaultWorker');oc(new tc)}
function pb(a){var b,c,d,e,f,g,h,i,j;j=yb(dc,Vc,0,a.length,0);for(e=0,f=j.length;e<f;++e){i=Nc(a[e],_c,0);b=-1;d='Unknown';if(i.length==2&&i[1]!=null){h=i[1];g=Lc(h,Rc(58));c=Mc(h,Rc(58),g-1);d=h.substr(0,c-0);if(g!=-1&&c!=-1){X(h.substr(c+1,g-(c+1)));b=X(Oc(h,g+1))}}j[e]=new Hc(i[0],d+ad+b)}A(j)}
function Nc(l,a,b){var c=new RegExp(a,'g');var d=[];var e=0;var f=l;var g=null;while(true){var h=c.exec(f);if(h==null||f==Yc||e==b-1&&b>0){d[e]=f;break}else{d[e]=f.substring(0,h.index);f=f.substring(h.index+h[0].length,f.length);c.lastIndex=0;if(g==f){d[e]=f.substring(0,1);f=f.substring(1)}g=f;e++}}if(b==0&&l.length>0){var i=d.length;while(i>0&&d[i-1]==Yc){--i}i<d.length&&d.splice(i,d.length-i)}var j=Qc(d.length);for(var k=0;k<d.length;++k){j[k]=d[k]}return j}
function mc(){var a,b,c;b=$doc.compatMode;a=zb(ec,Vc,1,[bd]);for(c=0;c<a.length;++c){if(Jc(a[c],b)){return}}a.length==1&&Jc(bd,a[0])&&Jc('BackCompat',b)?"GWT no longer supports Quirks Mode (document.compatMode=' BackCompat').<br>Make sure your application's host HTML page has a Standards Mode (document.compatMode=' CSS1Compat') doctype,<br>e.g. by using &lt;!doctype html&gt; at the start of your application's HTML page.<br><br>To continue using this unsupported rendering mode and risk layout problems, suppress this message by adding<br>the following line to your*.gwt.xml module file:<br>&nbsp;&nbsp;&lt;extend-configuration-property name=\"document.compatMode\" value=\""+b+'"/&gt;':"Your *.gwt.xml module configuration prohibits the use of the current doucment rendering mode (document.compatMode=' "+b+"').<br>Modify your application's host HTML page doctype, or update your custom 'document.compatMode' configuration property settings."}
var Yc='',cd=' ',Zc='(',ad='@',_c='@@',bd='CSS1Compat',fd='[Ljava.lang.',$c='anonymous',ed='com.google.gwt.core.client.',gd='com.google.gwt.core.client.impl.',dd='java.lang.';var _,ic={},Wc={2:1},Vc={};jc(1,-1,Vc);_.tM=Sc;jc(11,1,Wc);jc(10,11,Wc);jc(9,10,Wc);jc(8,9,Wc,C);_.b=null;jc(14,1,{});var E=0,F=0,G=-1;jc(16,14,{},T);_.b=null;_.c=null;var P;jc(19,1,{},ab);_.d=function bb(){var a={};var b=[];var c=arguments.callee.caller.caller;while(c){var d=this.e(c.toString());b.push(d);var e=':'+d;var f=a[e];if(f){var g,h;for(g=0,h=f.length;g<h;g++){if(f[g]===c){return b}}}(f||(a[e]=[])).push(c);c=c.caller}return b};_.e=function cb(a){return W(a)};_.f=function db(a){return []};jc(21,19,{});_.d=function hb(){return Y(this.f($()),this.g())};_.f=function ib(a){return gb(this,a)};_.g=function jb(){return 2};jc(20,21,{});_.d=function qb(){return lb(this)};_.e=function rb(a){var b,c,d,e;if(a.length==0){return $c}e=Pc(a);e.indexOf('at ')==0&&(e=Oc(e,3));c=e.indexOf('[');c!=-1&&(e=Pc(e.substr(0,c-0))+Pc(Oc(e,e.indexOf(']',c)+1)));c=e.indexOf(Zc);if(c==-1){c=e.indexOf(ad);if(c==-1){d=e;e=Yc}else{d=Pc(Oc(e,c+1));e=Pc(e.substr(0,c-0))}}else{b=e.indexOf(')',c);d=e.substr(c+1,b-(c+1));e=Pc(e.substr(0,c-0))}c=Kc(e,Rc(46));c!=-1&&(e=Oc(e,c+1));return (e.length>0?e:$c)+_c+d};_.f=function sb(a){return ob(this,a)};_.g=function tb(){return 3};jc(22,20,{},vb);jc(25,1,{},wb);var Ab,Bb;jc(35,1,{});_.b=null;jc(40,35,{},tc);_.i=function uc(a){var b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t;g=a.data;s=g.red_on;m=g.green_on;f=g.blue_on;b=g.A;c=g.B;r=g.red_map;l=g.green_map;e=g.blue_map;j=g.flip_m;i=g.flip_b;o=g.cpa;n=g.id;p=n.data;q=sc(s,b,c,j,i);k=sc(m,b,c,j,i);d=sc(f,b,c,j,i);t=o.length;for(h=0;h<t;h+=4){p[h+r]=q[o[h]||0];p[h+l]=k[o[h+1]||0];p[h+e]=d[o[h+2]||0];p[h+3]=255}pc(this.b,g)};jc(41,1,{},wc);_.b=0;jc(42,9,Wc,Dc);jc(43,9,Wc,Fc);jc(44,1,{},Hc);_=String.prototype;_.cM={1:1};var Xc=J;var Yb=yc(dd,'Object',1),Kb=yc(ed,'JavaScriptObject$',4),bc=xc(Yc,'[I',50),cc=xc(fd,'Object;',48),ac=yc(dd,'Throwable',11),Wb=yc(dd,'Exception',10),Zb=yc(dd,'RuntimeException',9),$b=yc(dd,'StackTraceElement',44),dc=xc(fd,'StackTraceElement;',51),Rb=yc('com.google.gwt.lang.','SeedUtil',31),Sb=yc('com.google.gwt.webworker.client.','DedicatedWorkerEntryPoint',35),Tb=yc('edu.ucsd.ncmir.WIB.DefaultWorker.worker.','DefaultWorker',40),Vb=yc(dd,'Class',41),_b=yc(dd,'String',2),ec=xc(fd,'String;',49),Ub=yc(dd,'ClassCastException',42),Jb=yc(ed,'JavaScriptException',8),Qb=yc(gd,'StackTraceCreator$Collector',19),Pb=yc(gd,'StackTraceCreator$CollectorMoz',21),Ob=yc(gd,'StackTraceCreator$CollectorChrome',20),Nb=yc(gd,'StackTraceCreator$CollectorChromeNoSourceMap',22),Lb=yc(ed,'Scheduler',14),Mb=yc(gd,'SchedulerImpl',16),Xb=yc(dd,'NullPointerException',43);