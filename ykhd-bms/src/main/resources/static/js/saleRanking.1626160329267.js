(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["saleRanking"],{"41a1":function(t,a,e){t.exports=e.p+"img/cup.fd396faf.png"},"4cad":function(t,a,e){"use strict";e("a330")},"619a":function(t,a,e){t.exports=e.p+"img/3.074526f6.png"},"71ca":function(t,a,e){"use strict";e.r(a);var n=function(){var t=this,a=t.$createElement,n=t._self._c||a;return n("div",{staticClass:"wedo-container"},[n("div",{staticClass:"wedo-container",staticStyle:{left:"10%",right:"10%"}},[n("vue-scroll",{ref:"vs",attrs:{ops:t.ops},on:{"handle-scroll":t.handleScroll}},[n("div",{staticClass:"padding10"},[n("a-form",{attrs:{layout:"inline"}},[n("a-row",{attrs:{gutter:48}},[n("a-col",{attrs:{md:24,sm:24}},[n("a-form-item",{attrs:{label:""}},[n("a-button",{staticClass:"marginR20",attrs:{size:"small"},on:{click:function(a){return t.timeChangeOn(1)}}},[t._v("今日")]),n("a-button",{staticClass:"marginR20",attrs:{size:"small"},on:{click:function(a){return t.timeChangeOn(2)}}},[t._v("昨日")]),n("a-button",{staticClass:"marginR20",attrs:{size:"small"},on:{click:function(a){return t.timeChangeOn(3)}}},[t._v("上月")]),n("a-button",{staticClass:"marginR20",attrs:{size:"small"},on:{click:function(a){return t.timeChangeOn(4)}}},[t._v("当月")])],1)],1),n("a-col",{attrs:{md:24,sm:24}},[n("a-form-item",{attrs:{label:"起始日期"}},[n("div",{staticClass:"flex",staticStyle:{"margin-top":"4px"}},[n("a-date-picker",{staticClass:"marginR10",attrs:{value:t.queryParam.startTime,format:t.dateFormat,placeholder:"起始日期"},on:{change:t.startDateOn}}),n("a-date-picker",{staticClass:"marginR10",attrs:{value:t.queryParam.endTime,format:t.dateFormat,placeholder:"结束日期"},on:{change:t.endDateOn}}),n("a-button",{attrs:{type:"primary"},on:{click:function(a){return t.getTableDataOn(!0)}}},[t._v("查询")])],1)])],1)],1)],1),n("div",{staticClass:"marginT10"},[n("a-table",{attrs:{pagination:!1,loading:t.tableLoading,rowKey:"index",columns:t.tableColumns,"data-source":t.tableDatas},scopedSlots:t._u([{key:"index",fn:function(a,r){return[0==r.index?n("img",{attrs:{src:e("c1c5")}}):1==r.index?n("img",{attrs:{src:e("81e5")}}):2==r.index?n("img",{attrs:{src:e("619a")}}):n("span",[t._v(t._s(r.index+1))])]}},{key:"name",fn:function(a){return[n("span",{staticClass:"main-text"},[t._v(t._s(a))])]}},{key:"dept",fn:function(a,r){return[n("span",[t._v(t._s(a))]),r.champTeam?n("p",{staticClass:"tuan-icon"},[n("span",[t._v("团冠")]),n("img",{attrs:{src:e("41a1")}})]):t._e()]}}])})],1)],1)])],1)])},r=[],i=(e("5880"),e("c1df")),s=e.n(i),o=e("77a5");function c(t,a){var e=Object.keys(t);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(t);a&&(n=n.filter((function(a){return Object.getOwnPropertyDescriptor(t,a).enumerable}))),e.push.apply(e,n)}return e}function m(t){for(var a=1;a<arguments.length;a++){var e=null!=arguments[a]?arguments[a]:{};a%2?c(Object(e),!0).forEach((function(a){l(t,a,e[a])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(e)):c(Object(e)).forEach((function(a){Object.defineProperty(t,a,Object.getOwnPropertyDescriptor(e,a))}))}return t}function l(t,a,e){return a in t?Object.defineProperty(t,a,{value:e,enumerable:!0,configurable:!0,writable:!0}):t[a]=e,t}var u={name:"saleRanking",components:{},data:function(){return{ops:{scrollPanel:{speed:0,scrollingX:!1},bar:{background:"#999"}},scrollTop:0,tableLoading:!1,tableColumns:[{title:"序号",dataIndex:"index",align:"center",width:"10%",scopedSlots:{customRender:"index"}},{title:"姓名",dataIndex:"name",align:"center",width:"15%",scopedSlots:{customRender:"name"}},{title:"战队",dataIndex:"dept",align:"center",width:"15%",scopedSlots:{customRender:"dept"}},{title:"排期次数",dataIndex:"count",align:"center",width:"15%"}],tableDatas:[],queryParam:{startTime:null,endTime:null},dateFormat:"YYYY-MM-DD"}},watch:{},mounted:function(){this.refreshData()},activated:function(){0!=this.scrollTop?this.scrollTo():this.scrolla()},methods:{moment:s.a,handleScroll:function(t,a,e){this.scrollTop=t.scrollTop},scrollTo:function(){this.$refs.vs.scrollTo({x:0,y:this.scrollTop},!0)},scrolla:function(){this.$refs.vs.scrollTo({x:0,y:0},!0)},manualmethods:function(){this.refreshData()},refreshData:function(){Object.assign(this.$data,this.$options.data());var t=s()().startOf("month").format(this.dateFormat),a=s()().format(this.dateFormat);this.queryParam.startTime=t,this.queryParam.endTime=a,this.getTableDataOn()},getTableDataOn:function(){var t=this;this.queryParam.startTime&&this.queryParam.endTime?(this.tableLoading=!0,o["a"].homeAERanking(m({},this.queryParam)).then((function(a){t.tableLoading=!1;var e=a.data;e.data&&e.data.length&&e.data.map((function(t,a){t.index=a})),t.tableDatas=e.data||[]})).catch((function(a){t.tableLoading=!1}))):wedo_tip("请输入日期",!1)},startDateOn:function(t,a){this.queryParam.startTime=a},endDateOn:function(t,a){this.queryParam.endTime=a},timeChangeOn:function(t){1==t&&(this.queryParam.startTime=s()().format(this.dateFormat),this.queryParam.endTime=s()().format(this.dateFormat)),2==t&&(this.queryParam.startTime=s()().subtract(1,"days").format(this.dateFormat),this.queryParam.endTime=s()().subtract(1,"days").format(this.dateFormat)),3==t&&(this.queryParam.startTime=s()().month(s()().month()-1).startOf("month").format(this.dateFormat),this.queryParam.endTime=s()().month(s()().month()-1).endOf("month").format(this.dateFormat)),4==t&&(this.queryParam.startTime=s()().startOf("month").format(this.dateFormat),this.queryParam.endTime=s()().format(this.dateFormat)),this.getTableDataOn()}}},d=u,h=(e("4cad"),e("2877")),f=Object(h["a"])(d,n,r,!1,null,"64fe52d5",null);a["default"]=f.exports},"77a5":function(t,a,e){"use strict";var n=e("e1d2");a["a"]={homeAERanking:function(t){return n["a"].get("home/AERanking",t)},homeMediumRanking:function(t){return n["a"].get("home/mediumRanking",t)},homeAEChampByMonthAndWeek:function(t){return n["a"].get("home/AEChampByMonthAndWeek",t)}}},"81e5":function(t,a,e){t.exports=e.p+"img/2.628ac49a.png"},a330:function(t,a,e){},c1c5:function(t,a,e){t.exports=e.p+"img/1.0c5576f1.png"}}]);