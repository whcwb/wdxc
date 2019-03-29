var index = {
  render: function () {
    var t = this, e = t.$createElement, i = t._self._c || e;
    return i("div", {
      staticClass: "ui-swipe-out",
      style: {width: t.winWidth + "px", height: t.thisHeight + "px"}
    }, [i("div", {
      staticClass: "ui-swipe-out-content",
      style: {
        transition: t.ending ? "all " + (t.operateWidth / t.speed > 200 ? t.operateWidth / t.speed < 50 ? 50 : 200 : t.operateWidth / t.speed) + "ms ease-out" : "",
        transform: "translate3d(" + t.offsetX + "px, 0, 0)",
        width: t.winWidth + "px"
      },
      on: {touchstart: t.touchStartHandler, touchmove: t.touchMoveHandler, touchend: t.touchEndHandler}
    }, [t._t("content")], 2), t._v(" "), i("div", {
      staticClass: "ui-swipe-out-operate",
      style: {width: t.operateWidth + "px"}
    }, [t._t("operate")], 2)])
  },
  staticRenderFns: [],
  name: "UiSwipeOut",
  props: {
    operateWidth: {type: [Number, String], required: !0}, value: {
      type: String, default: function () {
        return "off"
      }
    }, canSwipe: {type: Boolean, default: !0}, height: [Number, String]
  },
  data: function () {
    return {
      startX: 0,
      offsetX: 0,
      startY: 0,
      offsetY: 0,
      lastOffsetX: 0,
      winWidth: window.innerWidth,
      changed: !1,
      operateOpen: !1,
      speed: 0,
      ending: !1,
      currentTime: 0,
      shouldFastSlide: !1,
      firstin: !0,
      interval: null,
      thisActive: !0,
      disableFastSpeed: !1,
      innerHeight: 0,
      thisHeight: 0,
      active: !0
    }
  },
  created: function () {
    var t = this;
    this.thisHeight = this.height, this.$watch("value", function (e) {
      "on" === e ? t.open() : "off" === e && t.close()
    })
  },
  mounted: function () {
    var t = this;
    this.thisActive = this.active, this.$nextTick(function () {
      t.thisHeight || (t.thisHeight = t.$slots.content[0].elm.clientHeight)
    }), this.thisOperateWidth = Number(this.operateWidth), "on" === this.value ? this.offsetX = -this.thisOperateWidth : this.offsetX = 0
  },
  methods: {
    close: function () {
      var t = this;
      this.thisSwitch = "off", this.$nextTick(function () {
        t.offsetX = 0
      }), this.operateOpen = !1
    }, open: function () {
      this.thisSwitch = "on", this.fingerMove, this.thisOperateWidth, this.offsetX = -this.thisOperateWidth, this.operateOpen = !0
    }, touchStartHandler: function (t) {
      this.moved = !1, this.thisActive && this.canSwipe ? (this.startX = t.touches[0].pageX, this.startY = t.touches[0].pageY, this.lastOffsetX = 0, this.ending = !1) : this.ending = !0
    }, touchMoveHandler: function (t) {
      this.canSwipe && (this.moved = !0, Math.abs(this.startY - t.touches[0].pageY) / Math.abs(this.startX - t.touches[0].pageX) > 1 || t.preventDefault(), this.fingerMove = t.touches[0].pageX - this.startX, this.firstin && (Math.abs(t.touches[0].pageY - this.startY) / Math.abs(t.touches[0].pageX - this.startX) < 1 ? this.thisActive = !0 : this.thisActive = !1, this.firstin = !1), this.thisActive && (t.stopPropagation(), !1 === this.operateOpen ? t.touches[0].pageX - this.startX < -this.thisOperateWidth ? (this.disableFastSpeed = !0, this.renderOffsetX = -this.thisOperateWidth - (Math.abs(t.touches[0].pageX - this.startX) - this.thisOperateWidth) / 2) : t.touches[0].pageX - this.startX > 0 && t.touches[0].pageX - this.startX < 5 ? this.renderOffsetX = 0 : t.touches[0].pageX - this.startX > 20 ? this.renderOffsetX = 20 : this.renderOffsetX = t.touches[0].pageX - this.startX : (this.changed = !0, t.touches[0].pageX - this.startX < 0 ? this.renderOffsetX = -this.thisOperateWidth : t.touches[0].pageX - this.startX > this.thisOperateWidth ? this.renderOffsetX = 0 : this.renderOffsetX = t.touches[0].pageX - this.startX - this.thisOperateWidth), requestAnimationFrame(this.render)))
    }, render: function () {
      !1 === this.ending && (this.offsetX = this.renderOffsetX)
    }, touchEndHandler: function (t) {
      var e = this;
      if (this.canSwipe) {
        var i = (new Date).getTime();
        if (this.speed = Math.abs(this.offsetX) / (i - this.currentTime), this.shouldFastSlide = !0, this.firstin = !0, this.switcher = "on", !this.thisActive) return this.thisActive = !0, this.firstin = !0, void(this.shouldFastSlide = !1);
        if (this.ending = !0, !this.changed && this.operateOpen) return this.operateOpen = !1, this.offsetX = 0, this.changed = !1, this.$emit("input", "off"), this.$emit("change", "off"), this.$emit("click"), void(this.shouldFastSlide = !1);
        if (this.changed = !1, this.moved) {
          if (this.shouldFastSlide && this.offsetX <= 0 && !this.disableFastSpeed) return clearInterval(this.interval), this.operateOpen ? (this.operateOpen = !1, this.offsetX = 0, this.thisSwitch = "off", this.$emit("input", "off"), this.$emit("change", "off")) : (this.operateOpen = !0, this.offsetX = -this.thisOperateWidth - 30, setTimeout(function () {
            e.offsetX = -e.thisOperateWidth
          }, 200), this.thisSwitch = "on", this.$emit("input", "on"), this.$emit("change", "on")), void(this.shouldFastSlide = !1);
          if (this.disableFastSpeed = !1, this.offsetX > 10) return this.offsetX = -20, this.thisActive = !1, void setTimeout(function () {
            e.offsetX = 0, e.thisActive = !0
          }, 200);
          this.shouldFastSlide = !1, this.offsetX < -this.thisOperateWidth / 2 ? (this.$emit("input", "on"), this.$emit("change", "on"), this.operateOpen = !0, this.offsetX = -this.thisOperateWidth) : (this.$emit("input", "off"), this.$emit("change", "off"), this.operateOpen = !1, this.offsetX = 0), this.interval = null
        }
      }
    }
  },
  watch: {
    height: function (t) {
      this.thisHeight = t
    }
  }
};
export default index;
