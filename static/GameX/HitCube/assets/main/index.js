System.register("chunks:///_virtual/Attack.ts",["./rollupPluginModLoBabelHelpers.js","cc"],(function(t){var e,i,r,n,o,a,u,l,c,s,h,p,f,b;return{setters:[function(t){e=t.applyDecoratedDescriptor,i=t.inheritsLoose,r=t.initializerDefineProperty,n=t.assertThisInitialized},function(t){o=t.cclegacy,a=t._decorator,u=t.Prefab,l=t.Node,c=t.input,s=t.Input,h=t.instantiate,p=t.RigidBody,f=t.Vec3,b=t.Component}],execute:function(){var d,y,T,g,m,v,P,F,R;o._RF.push({},"7014f92uKBOKYoidMhVZs4V","Attack",void 0);var S=a.ccclass,w=a.property;t("Attack",(d=S("Attack"),y=w(u),T=w(l),d((v=e((m=function(t){function e(){for(var e,i=arguments.length,o=new Array(i),a=0;a<i;a++)o[a]=arguments[a];return e=t.call.apply(t,[this].concat(o))||this,r(e,"bulletPrefab",v,n(e)),r(e,"bulletBox",P,n(e)),r(e,"bulletSpeed",F,n(e)),r(e,"autoFireRate",R,n(e)),e.touching=!1,e.fireTimer=0,e}i(e,t);var o=e.prototype;return o.start=function(){c.on(s.EventType.TOUCH_START,this.onTouchStart,this),c.on(s.EventType.TOUCH_END,this.onTouchEnd,this)},o.update=function(t){this.touching&&(this.fireTimer+=t,this.fireTimer>=this.autoFireRate&&(this.doFire(),this.fireTimer=0))},o.onTouchStart=function(t){this.touching=!0,this.doFire(),this.fireTimer=0},o.onTouchEnd=function(t){this.touching=!1},o.doFire=function(){var t=h(this.bulletPrefab);t.setParent(this.bulletBox),t.setWorldPosition(this.node.getPosition()),t.getComponent(p).setLinearVelocity(new f(0,0,this.bulletSpeed))},e}(b)).prototype,"bulletPrefab",[y],{configurable:!0,enumerable:!0,writable:!0,initializer:null}),P=e(m.prototype,"bulletBox",[T],{configurable:!0,enumerable:!0,writable:!0,initializer:null}),F=e(m.prototype,"bulletSpeed",[w],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){return-30}}),R=e(m.prototype,"autoFireRate",[w],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){return.4}}),g=m))||g));o._RF.pop()}}}));

System.register("chunks:///_virtual/Brick.ts",["./rollupPluginModLoBabelHelpers.js","cc"],(function(t){var r,n,o,e;return{setters:[function(t){r=t.inheritsLoose},function(t){n=t.cclegacy,o=t._decorator,e=t.Component}],execute:function(){var c;n._RF.push({},"c4900MAtYdJ25HmaxJtl7ld","Brick",void 0);var i=o.ccclass;o.property,t("Brick",i("Brick")(c=function(t){function n(){return t.apply(this,arguments)||this}r(n,t);var o=n.prototype;return o.start=function(){},o.update=function(t){this.node.getPosition().y<=-1&&this.node.destroy()},n}(e))||c);n._RF.pop()}}}));

System.register("chunks:///_virtual/Bullet.ts",["./rollupPluginModLoBabelHelpers.js","cc"],(function(t){var e,n,o,r;return{setters:[function(t){e=t.inheritsLoose},function(t){n=t.cclegacy,o=t._decorator,r=t.Component}],execute:function(){var u;n._RF.push({},"719fcGJhb1NRZe7K3IdhfYg","Bullet",void 0);var c=o.ccclass;o.property,t("Bullet",c("Bullet")(u=function(t){function n(){return t.apply(this,arguments)||this}e(n,t);var o=n.prototype;return o.start=function(){},o.update=function(t){this.node.getPosition().y<=-1&&this.node.destroy()},n}(r))||u);n._RF.pop()}}}));

System.register("chunks:///_virtual/Camera.ts",["./rollupPluginModLoBabelHelpers.js","cc"],(function(e){var t,n,o,i,r,a,c,u,s;return{setters:[function(e){t=e.applyDecoratedDescriptor,n=e.inheritsLoose,o=e.initializerDefineProperty,i=e.assertThisInitialized},function(e){r=e.cclegacy,a=e._decorator,c=e.input,u=e.Input,s=e.Component}],execute:function(){var p,l,h;r._RF.push({},"a2b4dJNHk1ITpKAEBRBqu+Q","Camera",void 0);var v=a.ccclass,T=a.property;e("Camera",v("Camera")((h=t((l=function(e){function t(){for(var t,n=arguments.length,r=new Array(n),a=0;a<n;a++)r[a]=arguments[a];return t=e.call.apply(e,[this].concat(r))||this,o(t,"moveSpeedScale",h,i(t)),t}n(t,e);var r=t.prototype;return r.start=function(){c.on(u.EventType.TOUCH_START,this.onTouchStart,this),c.on(u.EventType.TOUCH_MOVE,this.onTouchMove,this),c.on(u.EventType.TOUCH_END,this.onTouchEnd,this)},r.update=function(e){},r.onTouchStart=function(e){},r.onTouchMove=function(e){var t=this.node.position;this.node.setPosition(t.x+e.getDeltaX()*this.moveSpeedScale,t.y+e.getDeltaY()*this.moveSpeedScale,t.z)},r.onTouchEnd=function(e){},t}(s)).prototype,"moveSpeedScale",[T],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){return.03}}),p=l))||p);r._RF.pop()}}}));

System.register("chunks:///_virtual/main",["./Attack.ts","./Brick.ts","./Bullet.ts","./Camera.ts"],(function(){return{setters:[null,null,null,null],execute:function(){}}}));

(function(r) {
  r('virtual:///prerequisite-imports/main', 'chunks:///_virtual/main'); 
})(function(mid, cid) {
    System.register(mid, [cid], function (_export, _context) {
    return {
        setters: [function(_m) {
            var _exportObj = {};

            for (var _key in _m) {
              if (_key !== "default" && _key !== "__esModule") _exportObj[_key] = _m[_key];
            }
      
            _export(_exportObj);
        }],
        execute: function () { }
    };
    });
});