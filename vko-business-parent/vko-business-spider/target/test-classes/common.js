function load_js(path) {
    setTimeout(function() {
        a = document.createElement("script"),
        a.type = "text/javascript",
        a.src = staticUrlName + "js/" + path + "?ver=" + (new Date).valueOf(),
        document.body.appendChild(a)
    },
    300)
}
var baseUrlName = $("#base-url-con").html(),
loginUrlName = $("#login-url-con").html(),
staticUrlName = $("#static-url-con").html(),
staticBaseUrlName = $("#static-base-url-con").html(),
baseSessID = $("#base-url-con").attr("tzid"),
baseUnID = $("#base-url-con").attr("tzu"),
Common = {
    defaultIndexHeight: function(id) {
        var biaozhun = $(window).height() - 85;
        $(id).height() < biaozhun && $(id).height(biaozhun)
    },
    getLeftBar: function(obj) {
        $(obj.id).css("height", $(document).height() - 80)
    },
    headerNav: function(json) {
        var _id = json.id,
        _ul = json.ul,
        _dis = json.dis;
        $(_id).hover(function() {
            $(this).find(_ul).addClass(_dis)
        },
        function() {})
    },
    selects: function(_id, clickFunc) {
        $(_id).click(function() {
            return $(this).offset().top > 340 && $(this).find("ul").show().css("top", "-241px"),
            $(this).find("ul").show(),
            $(this).parent().parent().css("z-index", "2").siblings().css("z-index", "1").find("ul").hide(),
            !1
        }),
        $("body").live("click",
        function() {
            $(_id).find("ul").hide()
        }),
        $(_id).each(function() {
            $(this).find("li").click(function(e) {
                e.stopPropagation();
                var _style = $(this).text();
                if ($(this).find("a").attr("data-id")) {
                    var _id = $(this).find("a").attr("data-id");
                    $(this).parent().prev("input").attr("data-id", _id)
                }
                $(this).parent().prev("input").val(_style),
                $(this).parent().hide(),
                void 0 != clickFunc && clickFunc()
            })
        })
    },
    hoverSelects: function(_id) {
        $(_id).each(function() {
            $(this).hover(function() {
                $(this).find("ul").show()
            },
            function() {
                $(this).find("ul").hide()
            })
        }),
        $(_id).each(function() {
            $(this).find("li").click(function(e) {
                e.stopPropagation();
                var _style = $(this).text();
                if ($(this).find("a").attr("data-id")) {
                    var _id = $(this).find("a").attr("data-id");
                    $(this).parent().prev("input").attr("data-id", _id)
                }
                $(this).parent().prev("input").val(_style),
                $(this).parent().hide()
            })
        })
    },
    inputTips: function(id) {
        var txt = $(id).val();
        $(id).focus(function() {
            "请输入关键字" == $(id).val() && $(id).val("")
        }),
        $(id).blur(function() {
            "" == $(id).val() && $(id).val(txt)
        })
    },
    aboutUs: function() {
        this.cReturnTopfn(),
        void 0 != $(".aboutUs .content").attr("pagename") && $(".aboutUs li").each(function(i) {
            $(".aboutUs .content").attr("pagename") == $(".aboutUs li").eq(i).attr("name") && $(".aboutUs li").eq(i).find("a").attr("class", "active")
        })
    },
    cReturnTopfn: function() {
        $(".cReturnTop").length < 1 && $("body").append('<div class="cReturnTop">返回顶部</div>'),
        $(window).scroll(function() {
            $(window).scrollTop() > 300 ? $(".cReturnTop").fadeIn(1500) : $(".cReturnTop").fadeOut(1500)
        }),
        $(".cReturnTop").click(function() {
            return $("body,html").animate({
                scrollTop: 0
            },
            1e3),
            !1
        })
    },
    reportGo: function(p) {
        var const_url = baseUrlName + "about/report/";
        window.location.href = const_url + p
    },
    fixQRCodeBar: function() {
        $(".QRcolse").live("click",
        function() {
            $(".QRCode").hide()
        })
    }
};
Common.vaildDataType = {
    URname: {
        datatype: "*2-20",
        nullmsg: "请输入真实姓名",
        errormsg: "长度2-20个字符之间"
    },
    Uname: {
        datatype: "/^[a-zA-Z]{1}\\w{5,17}$/",
        nullmsg: "请输入用户名",
        errormsg: "以英文字母开头，6-18个英文或数字"
    },
    Dname: {
        datatype: "/^[a-zA-Z]{1}[a-zA-Z_0-9]{3,17}$/",
        nullmsg: "请输入个性域名",
        errormsg: "以英文字母开头，4-18个英文，数字或下划线"
    },
    Oname: {
        datatype: "*2-20",
        nullmsg: "请输入单位名称",
        errormsg: "长度2-20个字符之间"
    },
    Email: {
        datatype: "e",
        nullmsg: "请输入邮箱地址",
        errormsg: "请输入正确的邮箱地址"
    },
    Phone: {
        datatype: "m",
        nullmsg: "请输入手机号码",
        errormsg: "请输入正确的手机号码"
    },
    PhoneCode: {
        datatype: "/^\\d{6}$/",
        datatype_allownull: "/^\\d{6}$/ | /^\\w{0}$/",
        nullmsg: "请输入验证码",
        errormsg: "验证码长度是6位"
    },
    CaptchaCode: {
        datatype: "/^\\w{4}$/",
        nullmsg: "请输入验证码",
        errormsg: "验证码长度是4位"
    },
    QQ: {
        datatype: "n5-12 | /^\\w{0}$/",
        datatype_nonull: "n5-12",
        nullmsg: "请输入QQ号码！",
        errormsg: "长度5-12个数字之间"
    },
    Passwd: {
        datatype: "*6-16",
        nullmsg: "请输入密码",
        errormsg: "长度6-16个字符之间"
    },
    Username: {
        datatype: "*6-30",
        nullmsg: "请输入邮箱/手机号/用户名/学号",
        errormsg: "长度6-30字符之间"
    },
    Classname: {
        datatype: "*1-12",
        nullmsg: "请输入班级名称",
        errormsg: "长度1-12字符之间"
    },
    resetFiles: {
        datatype: "*1-50",
        nullmsg: "请输入文件名称",
        errormsg: "长度1-50字符之间"
    },
    filenames: {
        datatype: "*1-50",
        nullmsg: "请输入文件夹名称",
        errormsg: "长度1-50字符之间"
    },
    shareFlies: {
        datatype: "*1-500",
        nullmsg: "请输入文件描述",
        errormsg: "长度1-500字符之间"
    },
    Classnum: {
        datatype: "*6-8",
        nullmsg: "请输入班级编号",
        errormsg: "长度6-8字符之间"
    },
    radioValid: {
        datatype: "*"
    }
},
Common.index = {
    chooseRegRole: function() {
        $(".regTeacherRole").click(function() {
            return Common.valid.resetForms(),
            Common.comValidform.changeCaptcha("RegTBox", ".changeCaptchaRegT"),
            $(".loginWrap,.regCon").hide(),
            $(".regTeacherCon").fadeIn(),
            !1
        }),
        $(".regStudentRole").click(function() {
            return Common.valid.resetForms(),
            Common.comValidform.changeCaptcha("RegSBox", ".changeCaptchaRegS"),
            $(".loginWrap,.regCon").hide(),
            $(".regStudentCon").fadeIn(),
            !1
        }),
        $(".regParentRole").click(function() {
            return Common.valid.resetForms(),
            Common.comValidform.changeCaptcha("RegPBox", ".changeCaptchaRegP"),
            $(".loginWrap,.regCon").hide(),
            $(".regParentCon").fadeIn(),
            !1
        }),
        $(".returnToLogin").click(function() {
            Common.valid.resetForms(),
            $(".regCon").hide(),
            $(".loginWrap").fadeIn()
        })
    },
    indexFocus: function() {
        var fs_A2 = function(id, Btn, ul, ms) {
            this.id = $(id),
            this.Btn = $(Btn),
            this.ul = $(ul),
            this.ulLi = $(ul).find("li"),
            this.ul.css("width", $(ul).find("li").eq(0).width() * $(ul).find("li").length + "px"),
            this.nextTarget = 0,
            this.autoTimer = null,
            this.ms = ms
        };
        fs_A2.prototype = {
            start: function() {
                var _this = this;
                this.Btn.each(function() {
                    var index = _this.Btn.index(this);
                    $(this).hover(function() {
                        _this.showSlides(index),
                        _this.nextTarget = index
                    })
                }),
                this.id.hover(function() {
                    clearInterval(_this.autoTimer)
                },
                function() {
                    _this.autoTimer = setInterval(function() {
                        _this.autoPlay()
                    },
                    _this.ms)
                }),
                clearInterval(this.autoTimer),
                this.autoTimer = setInterval(function() {
                    _this.autoPlay()
                },
                this.ms)
            },
            showSlides: function(index) {
                this.Btn.eq(index).addClass("active").siblings().removeClass("active"),
                this.ul.animate({
                    "margin-left": -this.ul.find("li").eq(0).width() * index + "px"
                })
            },
            autoPlay: function() {
                this.nextTarget++,
                this.nextTarget > this.ulLi.length - 1 && (this.nextTarget = 0),
                this.showSlides(this.nextTarget)
            }
        },
        new fs_A2("#wind_A2", "#btn a", "#wind_A2 ul", 5e3).start()
    }
},
Common.comValidform = {
    md5: function(element) {
        var i = 0;
        element.find("input").each(function() {
            if ("password" == $(this).attr("type")) {
                var input_name = $(this).attr("name");
                void 0 == input_name && (input_name = $(this).attr("md5_name")),
                input_name && (i += 1);
                var input_id = input_name + i,
                pass_hidden = '<input class="text-input" id="' + input_id + '_md5" name="' + input_name + '" type="hidden">';
                $("#" + input_id + "_md5").remove(),
                $(this).parent().append(pass_hidden),
                $("#" + input_id + "_md5").val(tizi_md5($(this).val())),
                $(this).attr("md5_name", input_name).removeAttr("name")
            }
        })
    },
    reset_md5: function(element) {
        var i = 0;
        $(element).find("input").each(function() {
            if ("password" == $(this).attr("type")) {
                input_name = $(this).attr("md5_name"),
                input_name && (i += 1);
                var input_id = input_name + i;
                $("#" + input_id + "_md5").remove(),
                $(this).attr("name", input_name).removeAttr("md5_name")
            }
        })
    },
    changeCaptcha: function(page_name, element) {
        if (void 0 == page_name && (page_name = $(".pname").attr("id")), element) var img = $(element).siblings("img");
        else var img = $(".changeCaptcha").siblings("img");
        var now = (new Date).valueOf(),
        url = baseUrlName + "captcha?page_name=" + page_name + "&ver=" + now;
        img.attr("src", url)
    },
    bindChangeVerify: function(page_name, element) {
        element ? $(element + ", " + element + "imgCaptcha").live("click",
        function(event) {
            event.preventDefault(),
            Common.comValidform.changeCaptcha(page_name, element)
        }) : $(".changeCaptcha, .imgCaptcha").live("click",
        function(event) {
            event.preventDefault(),
            Common.comValidform.changeCaptcha(page_name, element)
        })
    },
    sendPhoneCode: function(phone, code_type, fn, errfn) {
        if (phone) {
            var url = baseUrlName + "send_phone_code";
            $.tizi_ajax({
                url: url,
                type: "post",
                dataType: "json",
                data: {
                    phone: phone,
                    code_type: code_type,
                    ver: (new Date).valueOf()
                },
                success: function(data) {
                    data.errorcode ? fn(data) : errfn(data)
                }
            })
        }
    },
    sendEmailCode: function(email, code_type, fn, errfn) {
        if (email) {
            var url = baseUrlName + "send_email_code";
            $.tizi_ajax({
                url: url,
                type: "post",
                dataType: "json",
                data: {
                    email: email,
                    code_type: code_type,
                    ver: (new Date).valueOf()
                },
                success: function(data) {
                    data.errorcode ? fn() : errfn(data)
                }
            })
        }
    },
    checkCaptcha: function(checkcode, keep_code, show_dialog, page_name) {
        var url = baseUrlName + "check_captcha",
        check = !1;
        return void 0 == page_name && (page_name = $(".pname").attr("id")),
        $.ajax({
            url: url,
            type: "get",
            dataType: "json",
            async: !1,
            data: {
                check_code: checkcode,
                keep_code: keep_code,
                page_name: page_name,
                ver: (new Date).valueOf()
            },
            success: function(data) {
                data.errorcode ? ($(".textCaptcha").siblings(".Validform_checktip").text(data.error).attr("class", "Validform_checktip Validform_right"), check = !0) : (show_dialog && $.tiziDialog({
                    content: data.error,
                    time: 3
                }), $(".textCaptcha").siblings(".Validform_checktip").text(data.error).attr("class", "Validform_checktip Validform_wrong"), check = !1)
            }
        }),
        check
    },
    checkPhoneCode: function(checkcode, phone, code_type) {
        var url = baseUrlName + "check_code",
        check = !1;
        return $.tizi_ajax({
            url: url,
            type: "post",
            dataType: "json",
            async: !1,
            data: {
                phone: phone,
                check_code: checkcode,
                code_type: code_type,
                ver: (new Date).valueOf()
            },
            success: function(data) {
                data.errorcode ? ($(".phoneCode").siblings(".Validform_checktip").text(data.error).attr("class", "Validform_checktip Validform_right"), check = !0) : ($(".phoneCode").siblings(".Validform_checktip").text(data.error).attr("class", "Validform_checktip Validform_wrong"), check = !1)
            }
        }),
        check
    },
    detectFlashSupport: function(fn_noflash, fn_flash) {
        void 0 == fn_noflash && (fn_noflash = function() {}),
        void 0 == fn_flash && (fn_flash = function() {});
        var hasFlash = !1;
        if ("function" == typeof ActiveXObject) try {
            new ActiveXObject("ShockwaveFlash.ShockwaveFlash") && (hasFlash = !0)
        } catch(error) {} ! hasFlash && navigator.mimeTypes["application/x-shockwave-flash"] && (hasFlash = !0),
        hasFlash ? fn_flash() : fn_noflash()
    },
    checkLogin: function() {
        var username = $.cookies.get(baseUnID);
        username && $.tizi_ajax({
            url: baseUrlName + "login/login/check_login",
            type: "get",
            dataType: "json",
            data: {},
            success: function(data) {
                data.errorcode && (window.location.href = baseUrlName)
            }
        })
    },
    checkStudent: function(fn_success) {
        void 0 == fn_success && (fn_success = function() {}),
        $.tizi_ajax({
            url: baseUrlName + "user/user_info/auth_register",
            type: "get",
            dataType: "json",
            data: {},
            success: function(data) {
                data.errorcode ? data.show_register ? ($.tiziDialog({
                    id: "regStudentFormID",
                    title: "绑定学生账号",
                    content: data.html,
                    icon: null,
                    width: 380,
                    ok: function() {
                        return $("#a_login").val(0),
                        $(".regStudentForm").submit(),
                        !1
                    },
                    cancel: !0
                }), Common.valid.init(), Common.comValidform.changeCaptcha(), Common.comValidform.bindChangeVerify()) : fn_success() : $.tiziDialog({
                    content: data.error
                })
            }
        })
    }
},
Common.valid = {
    init: function() {
        this.login(),
        this.reg.init()
    },
    login: function() {
        var _Form = $(".indexLoginForm").Validform({
            tiptype: function(msg, o) {
                if (!o.obj.is("form")) {
                    var objtip = o.obj.next().find(".Validform_checktip");
                    objtip.text(msg),
                    o.obj.next().show();
                    var objtip = o.obj.next().find(".Validform_checktip");
                    objtip.text(msg);
                    var infoObj = o.obj.next(".ValidformTips");
                    2 == o.type && (infoObj.show(), o.obj.next().hide())
                }
            },
            showAllError: !1,
            beforeSubmit: function(curform) {
                Common.comValidform.md5(curform)
            },
            ajaxPost: !0,
            callback: function(data) {
                CommonAjax.Login.submit(data)
            }
        });
        _Form.addRule([{
            ele: ".username",
            datatype: Common.vaildDataType.Username.datatype,
            nullmsg: Common.vaildDataType.Username.nullmsg,
            errormsg: Common.vaildDataType.Username.errormsg
        },
        {
            ele: ".password",
            datatype: Common.vaildDataType.Passwd.datatype,
            nullmsg: Common.vaildDataType.Passwd.nullmsg,
            errormsg: Common.vaildDataType.Passwd.errormsg
        }])
    },
    reg: {
        init: function() {
            this.teacher(),
            this.student(),
            this.parent()
        },
        teacher: function() {
            var _Form = $(".regTeacherForm").Validform({
                tiptype: function(msg, o) {
                    if (!o.obj.is("form")) {
                        var objtip = o.obj.next().find(".Validform_checktip");
                        objtip.text(msg),
                        o.obj.next().show();
                        var objtip = o.obj.next().find(".Validform_checktip");
                        objtip.text(msg);
                        var infoObj = o.obj.next(".ValidformInfo");
                        2 == o.type && (infoObj.show().fadeOut(), o.obj.next().hide())
                    }
                },
                showAllError: !1,
                beforeSubmit: function(curform) {
                    Common.comValidform.md5(curform);
                    var checkcode = $(".teacherCaptap").val();
                    return Common.comValidform.checkCaptcha(checkcode, 1, 1, "RegTBox")
                },
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.Register.submit(data, "RegTBox", ".changeCaptchaRegT")
                }
            });
            _Form.addRule([{
                ele: ".teacherName",
                datatype: Common.vaildDataType.Email.datatype,
                nullmsg: Common.vaildDataType.Email.nullmsg,
                errormsg: Common.vaildDataType.Email.errormsg
            },
            {
                ele: ".teacherPassword",
                datatype: Common.vaildDataType.Passwd.datatype,
                nullmsg: Common.vaildDataType.Passwd.nullmsg,
                errormsg: Common.vaildDataType.Passwd.errormsg
            },
            {
                ele: ".teacherNames",
                datatype: Common.vaildDataType.URname.datatype,
                nullmsg: Common.vaildDataType.URname.nullmsg,
                errormsg: Common.vaildDataType.URname.errormsg
            },
            {
                ele: ".teacherCaptap",
                datatype: Common.vaildDataType.CaptchaCode.datatype,
                nullmsg: Common.vaildDataType.CaptchaCode.nullmsg,
                errormsg: Common.vaildDataType.CaptchaCode.errormsg
            },
            {
                ele: ":checkbox:first",
                datatype: "*",
                nullmsg: "请先同意协议",
                errormsg: "您未同意协议！"
            }])
        },
        student: function() {
            var _Form = $(".regStudentForm").Validform({
                tiptype: function(msg, o) {
                    if (!o.obj.is("form")) {
                        var objtip = o.obj.next().find(".Validform_checktip");
                        objtip.text(msg),
                        o.obj.next().show();
                        var objtip = o.obj.next().find(".Validform_checktip");
                        objtip.text(msg);
                        var infoObj = o.obj.next(".ValidformInfo");
                        2 == o.type && (infoObj.show(), o.obj.next().hide())
                    }
                },
                showAllError: !1,
                beforeSubmit: function(curform) {
                    Common.comValidform.md5(curform);
                    var checkcode = $(".studentCaptap").val();
                    return Common.comValidform.checkCaptcha(checkcode, 1, 1, "RegSBox")
                },
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.Register.submit(data, "RegSBox", ".changeCaptchaRegS")
                }
            });
            _Form.addRule([{
                ele: ".studentName",
                datatype: Common.vaildDataType.Uname.datatype,
                nullmsg: Common.vaildDataType.Uname.nullmsg,
                errormsg: Common.vaildDataType.Uname.errormsg
            },
            {
                ele: ".studentPassword",
                datatype: Common.vaildDataType.Passwd.datatype,
                nullmsg: Common.vaildDataType.Passwd.nullmsg,
                errormsg: Common.vaildDataType.Passwd.errormsg
            },
            {
                ele: ".studentNames",
                datatype: Common.vaildDataType.URname.datatype,
                nullmsg: Common.vaildDataType.URname.nullmsg,
                errormsg: Common.vaildDataType.URname.errormsg
            },
            {
                ele: ".studentQQ",
                datatype: Common.vaildDataType.QQ.datatype,
                errormsg: Common.vaildDataType.QQ.errormsg
            },
            {
                ele: ".studentCaptap",
                datatype: Common.vaildDataType.CaptchaCode.datatype,
                nullmsg: Common.vaildDataType.CaptchaCode.nullmsg,
                errormsg: Common.vaildDataType.CaptchaCode.errormsg
            },
            {
                ele: ":checkbox:first",
                datatype: "*",
                nullmsg: "请先同意协议",
                errormsg: "您未同意协议！"
            }])
        },
        parent: function() {
            var _Form = $(".regParentForm").Validform({
                tiptype: function(msg, o) {
                    if (!o.obj.is("form")) {
                        var objtip = o.obj.next().find(".Validform_checktip");
                        objtip.text(msg),
                        o.obj.next().show();
                        var objtip = o.obj.next().find(".Validform_checktip");
                        objtip.text(msg);
                        var infoObj = o.obj.next(".ValidformInfo");
                        2 == o.type && (infoObj.show(), o.obj.next().hide())
                    }
                },
                showAllError: !1,
                beforeSubmit: function(curform) {
                    Common.comValidform.md5(curform);
                    var checkcode = $(".parentCaptap").val();
                    return Common.comValidform.checkCaptcha(checkcode, 1, 1, "RegPBox")
                },
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.Register.submit(data, "RegPBox", ".changeCaptchaRegP")
                }
            });
            _Form.addRule([{
                ele: ".parentName",
                datatype: Common.vaildDataType.Email.datatype,
                nullmsg: Common.vaildDataType.Email.nullmsg,
                errormsg: Common.vaildDataType.Email.errormsg
            },
            {
                ele: ".parentPassword",
                datatype: Common.vaildDataType.Passwd.datatype,
                nullmsg: Common.vaildDataType.Passwd.nullmsg,
                errormsg: Common.vaildDataType.Passwd.errormsg
            },
            {
                ele: ".parentNames",
                datatype: Common.vaildDataType.URname.datatype,
                nullmsg: Common.vaildDataType.URname.nullmsg,
                errormsg: Common.vaildDataType.URname.errormsg
            },
            {
                ele: ".parentCaptap",
                datatype: Common.vaildDataType.CaptchaCode.datatype,
                nullmsg: Common.vaildDataType.CaptchaCode.nullmsg,
                errormsg: Common.vaildDataType.CaptchaCode.errormsg
            },
            {
                ele: ":checkbox:first",
                datatype: "*",
                nullmsg: "请先同意协议",
                errormsg: "您未同意协议！"
            }])
        }
    },
    resetForms: function() {
        $(".indexLoginForm").Validform().resetForm(),
        $(".regTeacherForm").Validform().resetForm(),
        $(".regStudentForm").Validform().resetForm(),
        $(".regParentForm").Validform().resetForm(),
        $(".ValidformInfo").hide()
    },
    firstLogin: function() {
        var _Form = $(".firstLoginForm").Validform({
            tiptype: 3,
            showAllError: !1,
            ajaxPost: !0,
            callback: function(data) {
                CommonAjax.stuFirstLogin.submit(data)
            }
        });
        _Form.addRule([{
            ele: ".firstLoginName",
            datatype: Common.vaildDataType.Uname.datatype,
            nullmsg: Common.vaildDataType.Uname.nullmsg,
            errormsg: Common.vaildDataType.Uname.errormsg
        },
        {
            ele: ".firstLoginURname",
            datatype: Common.vaildDataType.URname.datatype,
            nullmsg: Common.vaildDataType.URname.nullmsg,
            errormsg: Common.vaildDataType.URname.errormsg
        },
        {
            ele: ".firstLoginKidURname",
            datatype: Common.vaildDataType.URname.datatype,
            nullmsg: Common.vaildDataType.URname.nullmsg,
            errormsg: Common.vaildDataType.URname.errormsg
        },
        {
            ele: ".firstLoginDname",
            datatype: Common.vaildDataType.Dname.datatype,
            nullmsg: Common.vaildDataType.Dname.nullmsg,
            errormsg: Common.vaildDataType.Dname.errormsg
        },
        {
            ele: ".firstLoginOname",
            datatype: Common.vaildDataType.Oname.datatype,
            nullmsg: Common.vaildDataType.Oname.nullmsg,
            errormsg: Common.vaildDataType.Oname.errormsg
        }])
    },
    forgetPwName: function() {
        var _Form = $(".forgetPwNameForm").Validform({
            tiptype: 3,
            showAllError: !1,
            ajaxPost: !0,
            beforeSubmit: function() {
                var checkcode = $(".forgetNameCaptap").val();
                return Common.comValidform.checkCaptcha(checkcode, 1, 0, "ForgotBox")
            },
            callback: function(data) {
                CommonAjax.findPassword.reset_mode(data)
            }
        });
        _Form.addRule([{
            ele: ".forgetPwName",
            datatype: Common.vaildDataType.Username.datatype,
            nullmsg: Common.vaildDataType.Username.nullmsg,
            errormsg: Common.vaildDataType.Username.errormsg
        },
        {
            ele: ".forgetNameCaptap",
            datatype: Common.vaildDataType.CaptchaCode.datatype,
            nullmsg: Common.vaildDataType.CaptchaCode.nullmsg,
            errormsg: Common.vaildDataType.CaptchaCode.errormsg
        }])
    },
    forgetPwTel: function() {
        var _Form = $(".forgetPwTelForm").Validform({
            tiptype: 3,
            showAllError: !1,
            ajaxPost: !0,
            beforeSubmit: function() {
                var checkcode = $(".forgetTelCaptap").val(),
                phone = $(".forgetTelNum").val();
                return Common.comValidform.checkPhoneCode(checkcode, phone, 2)
            },
            callback: function(data) {
                CommonAjax.findPassword.reset(data)
            }
        });
        _Form.addRule([{
            ele: ".forgetTelCaptap",
            datatype: Common.vaildDataType.PhoneCode.datatype,
            nullmsg: Common.vaildDataType.PhoneCode.nullmsg,
            errormsg: Common.vaildDataType.PhoneCode.errormsg
        }])
    },
    findPwOnlyTel: function() {
        var _Form = $(".findPwOnlyTelForm").Validform({
            tiptype: 3,
            showAllError: !1,
            ajaxPost: !0,
            callback: function(data) {
                CommonAjax.findPassword.apply(data)
            }
        });
        _Form.addRule([{
            ele: ".findPwOnlyTelName",
            datatype: Common.vaildDataType.Phone.datatype,
            nullmsg: Common.vaildDataType.Phone.nullmsg,
            errormsg: Common.vaildDataType.Phone.errormsg
        }])
    },
    restPassword: function() {
        var _Form = $(".restPasswordForm").Validform({
            tiptype: 3,
            showAllError: !1,
            beforeSubmit: function(curform) {
                Common.comValidform.md5(curform)
            },
            ajaxPost: !0,
            callback: function(data) {
                Common.comValidform.reset_md5(".restPasswordForm"),
                CommonAjax.findPassword.submit(data)
            }
        });
        _Form.addRule([{
            ele: ".setPassword",
            datatype: Common.vaildDataType.Passwd.datatype,
            nullmsg: Common.vaildDataType.Passwd.nullmsg,
            errormsg: Common.vaildDataType.Passwd.errormsg
        },
        {
            ele: ".reSetPassword",
            datatype: Common.vaildDataType.Passwd.datatype,
            recheck: "password",
            nullmsg: "再输入一次密码",
            errormsg: "两次输入不一致！"
        }])
    },
    bindMySubject: function() {
        $(".regTeacherSuccessForm").Validform({
            tiptype: 3,
            showAllError: !1,
            beforeSubmit: function() {
                return $(".regTeacherSuccess .active").length > 0 ? ($(".bindMySubject").val($(".regTeacherSuccess .active").attr("sid")), void 0) : ($.tiziDialog({
                    content: "请选择一个您的默认学科，在使用组卷功能时将默认看到该学科内容，默认学科可以进入个人中心修改。",
                    icon: null
                }), !1)
            },
            ajaxPost: !0,
            callback: function(data) {
                CommonAjax.Register.mysubject(data)
            }
        })
    },
    cardFirstLogin: function() {
        var _Form = $(".cardFirstLoginForm").Validform({
            tiptype: 3,
            showAllError: !1,
            beforeSubmit: function() {
                return $(".regTeacherSuccess .active").length > 0 ? ($(".bindMySubject").val($(".regTeacherSuccess .active").attr("sid")), void 0) : ($.tiziDialog({
                    content: "请选择一个您的默认学科，在使用组卷功能时将默认看到该学科内容，默认学科可以进入个人中心修改。",
                    icon: null
                }), !1)
            },
            ajaxPost: !0,
            callback: function(data) {
                CommonAjax.Register.perfect_teacher(data)
            }
        });
        _Form.addRule([{
            ele: ".cardEmail",
            datatype: Common.vaildDataType.Email.datatype,
            nullmsg: Common.vaildDataType.Email.nullmsg,
            errormsg: Common.vaildDataType.Email.errormsg
        },
        {
            ele: ".cardUserName",
            datatype: Common.vaildDataType.URname.datatype,
            nullmsg: Common.vaildDataType.URname.nullmsg,
            errormsg: Common.vaildDataType.URname.errormsg
        }])
    },
    cardFirstLogin_mobile: function() {
        var _Form = $(".cardFirstLoginForm").Validform({
            tiptype: function(msg, o) {
                if (!o.obj.is("form")) {
                    var objtip = o.obj.next().find(".Validform_checktip");
                    objtip.text(msg),
                    o.obj.next().show();
                    var objtip = o.obj.next().find(".Validform_checktip");
                    objtip.text(msg);
                    var infoObj = o.obj.next(".ValidformInfo");
                    2 == o.type && (infoObj.show().fadeOut(), o.obj.next().hide())
                }
            },
            showAllError: !1,
            beforeSubmit: function() {
                return $(".regTeacherSuccess .active").length > 0 ? ($(".bindMySubject").val($(".regTeacherSuccess .active").attr("sid")), void 0) : ($.tiziDialog({
                    content: "请选择一个您的默认学科，在使用组卷功能时将默认看到该学科内容，默认学科可以进入个人中心修改。",
                    icon: null
                }), !1)
            },
            ajaxPost: !0,
            callback: function(data) {
                CommonAjax.Register.perfect_teacher(data)
            }
        });
        _Form.addRule([{
            ele: ".cardEmail",
            datatype: Common.vaildDataType.Email.datatype,
            nullmsg: Common.vaildDataType.Email.nullmsg,
            errormsg: Common.vaildDataType.Email.errormsg
        },
        {
            ele: ".cardUserName",
            datatype: Common.vaildDataType.URname.datatype,
            nullmsg: Common.vaildDataType.URname.nullmsg,
            errormsg: Common.vaildDataType.URname.errormsg
        },
        {
            ele: ".cardPhone",
            datatype: Common.vaildDataType.Phone.datatype,
            nullmsg: Common.vaildDataType.Phone.nullmsg,
            errormsg: Common.vaildDataType.Phone.errormsg
        }])
    },
    bindMyGrade: function() {
        $(".regStudentSuccessForm").Validform({
            tiptype: 3,
            showAllError: !1,
            beforeSubmit: function() {
                return $(".regStudentSuccess .active").length > 0 ? ($(".bindMyGrade").val($(".regStudentSuccess .active").attr("gid")), void 0) : ($.tiziDialog({
                    content: "请选择你的年级，系统会推送与您年级相关的学习内容，你也可以进入个人中心修改年级。",
                    icon: null
                }), !1)
            },
            ajaxPost: !0,
            callback: function(data) {
                CommonAjax.Register.mygrade(data)
            }
        })
    },
    bindMyUname: function() {
        var _Form = $(".firstLoginForm").Validform({
            tiptype: 3,
            showAllError: !1,
            ajaxPost: !0,
            callback: function(data) {
                CommonAjax.Register.myuname(data)
            }
        });
        _Form.addRule([{
            ele: ".bindMyUname",
            datatype: Common.vaildDataType.Uname.datatype,
            nullmsg: Common.vaildDataType.Uname.nullmsg,
            errormsg: Common.vaildDataType.Uname.errormsg
        }])
    },
    bindAqStart: function() {
        $(".aq_startForm").Validform({
            tiptype: 3,
            showAllError: !1,
            ajaxPost: !0,
            beforeSubmit: function() {
                var subject_id = ($("#aq_question_id").val(), $("#aq_subject").val()),
                grade = $("#aq_grades").val(),
                content = $("#content").html(),
                picture_urls = "";
                return $(".picture_urls").each(function() {
                    picture_urls += "" === picture_urls ? "": ",",
                    picture_urls += $(this).attr("src")
                }),
                $("#picture_urls").val(picture_urls),
                $("#textareaContent").val(content),
                0 == subject_id || 0 == grade ? ($.tiziDialog({
                    content: "请选择科目和年级."
                }), !1) : "" === $.trim(content) ? ($.tiziDialog({
                    content: "提问内容不能为空"
                }), !1) : void 0
            },
            callback: function(data) {
                CommonAjax.Aq.AqStart(data)
            }
        })
    },
    TeacherGroup: {
        AddDocForm: function() {
            var _Form = $(".addDocForm").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                beforeSubmit: function() {},
                callback: function(data) {
                    CommonAjax.TeacherGroup.AddDocForm(data)
                }
            });
            _Form.addRule([{
                ele: ".group_name",
                datatype: "*2-20",
                nullmsg: "分组名称不能为空",
                errormsg: "请输入2-20个字符"
            }])
        },
        EditDocForm: function(that) {
            var _Form = $(".editDocForm").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                beforeSubmit: function() {},
                callback: function(data) {
                    CommonAjax.TeacherGroup.EditDocForm(data, that)
                }
            });
            _Form.addRule([{
                ele: ".group_name",
                datatype: "*2-20",
                nullmsg: "分组名称不能为空",
                errormsg: "请输入2-20个字符"
            }])
        }
    },
    paperHaveError: {
        addPaperHaveError: function() {
            var _Form = $(".haveErrorForm").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                beforeSubmit: function() {
                    var picture_urls = "";
                    $(".picture_urls").each(function() {
                        picture_urls += "" === picture_urls ? "": ",",
                        picture_urls += $(this).attr("src")
                    }),
                    $("#picture_urls").val(picture_urls)
                },
                callback: function(data) {
                    CommonAjax.paperHaveError.addPaperHaveError(data)
                }
            });
            _Form.addRule([{
                ele: ".lite_textarea",
                datatype: "*",
                nullmsg: "错误描述不能为空",
                errormsg: "错误描述不能为空"
            },
            {
                ele: ":checkbox:first",
                datatype: "*",
                nullmsg: "错误类型不能为空",
                errormsg: "错误类型不能为空"
            }])
        }
    },
    intelligent: {
        init: function() {},
        selects: function(_id, clickFunc) {
            $(_id).on("change", "select",
            function(event) {
                event.stopPropagation();
                $(this).text();
                if ($(this).find("option:selected").attr("value")) {
                    var _val = $(this).find("option:selected").attr("value");
                    $(this).prev("input").attr("value", _val)
                }
                void 0 != clickFunc && clickFunc()
            })
        }
    },
    teacherModifyMyInfo: {
        init: function() {
            this.modifyMyName(),
            this.modifyEmail(),
            this.modifyPhone(),
            this.modifyMyPassword()
        },
        reSetInfoForm: function() {
            $(".modifyMyNameForm").Validform().resetForm(),
            $(".modifyMyEmailForm").Validform().resetForm(),
            $(".modifyMyPhoneForm").Validform().resetForm(),
            $(".modifyMyPasswordForm").Validform().resetForm()
        },
        modifyMyName: function() {
            var _Form = $(".modifyMyNameForm").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.modifyInfo.modifyMyName(data)
                }
            });
            _Form.addRule([{
                ele: ".modifyUserName",
                datatype: Common.vaildDataType.URname.datatype,
                nullmsg: Common.vaildDataType.URname.nullmsg,
                errormsg: Common.vaildDataType.URname.errormsg
            }])
        },
        modifyEmail: function() {
            var _Form = $(".modifyMyEmailForm").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.modifyInfo.modifyEmail(data)
                }
            });
            _Form.addRule([{
                ele: ".modifyEmail",
                datatype: Common.vaildDataType.Email.datatype,
                nullmsg: Common.vaildDataType.Email.nullmsg,
                errormsg: Common.vaildDataType.Email.errormsg
            }])
        },
        modifyPhone: function() {
            var _Form = $(".modifyMyPhoneForm").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.modifyInfo.modifyPhone(data)
                }
            });
            _Form.addRule([{
                ele: ".modifyPhone",
                datatype: Common.vaildDataType.Phone.datatype,
                nullmsg: Common.vaildDataType.Phone.nullmsg,
                errormsg: Common.vaildDataType.Phone.errormsg
            },
            {
                ele: ".forgetTelCaptap",
                datatype: Common.vaildDataType.PhoneCode.datatype,
                nullmsg: Common.vaildDataType.PhoneCode.nullmsg,
                errormsg: Common.vaildDataType.PhoneCode.errormsg
            }])
        },
        authPhone: function() {
            var _Form = $(".authMyPhoneForm").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                callback: function(data) {
                    if (data.errorcode) {
                        var gn = $("body").attr("class");
                        ga("send", "event", "Auth-Download-" + gn, "Auth", "Binding complete")
                    }
                    CommonAjax.modifyInfo.modifyPhone(data)
                }
            });
            _Form.addRule([{
                ele: ".modifyPhone",
                datatype: Common.vaildDataType.Phone.datatype,
                nullmsg: Common.vaildDataType.Phone.nullmsg,
                errormsg: Common.vaildDataType.Phone.errormsg
            },
            {
                ele: ".forgetTelCaptap",
                datatype: Common.vaildDataType.PhoneCode.datatype,
                nullmsg: Common.vaildDataType.PhoneCode.nullmsg,
                errormsg: Common.vaildDataType.PhoneCode.errormsg
            },
            {
                ele: ".forgetTelCaptap_nocheck",
                datatype: Common.vaildDataType.PhoneCode.datatype_allownull,
                errormsg: Common.vaildDataType.PhoneCode.errormsg
            }])
        },
        modifyMyPassword: function() {
            var _Form = $(".modifyMyPasswordForm").Validform({
                tiptype: 3,
                showAllError: !1,
                beforeSubmit: function(curform) {
                    Common.comValidform.md5(curform)
                },
                ajaxPost: !0,
                callback: function(data) {
                    Common.comValidform.reset_md5(".modifyMyPasswordForm"),
                    CommonAjax.modifyInfo.modifyMyPassword(data)
                }
            });
            _Form.addRule([{
                ele: ".modifyMyPassword",
                datatype: Common.vaildDataType.Passwd.datatype,
                nullmsg: "请输入旧密码",
                errormsg: "旧密码输入错误！"
            },
            {
                ele: ".modifyMyNewPassword",
                datatype: Common.vaildDataType.Passwd.datatype,
                nullmsg: "请输入新密码",
                errormsg: "新密码输入错误！"
            },
            {
                ele: ".modifyMyReNewPassword",
                datatype: Common.vaildDataType.Passwd.datatype,
                recheck: "new-password",
                nullmsg: "请确认新密码",
                errormsg: "新密码确认错误！"
            }])
        }
    },
    dropbox: {
        creat: function() {
            var _Form = $(".creatNewFileForm").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.dropBox.creat(data)
                }
            });
            _Form.addRule([{
                ele: "#create_fileName",
                datatype: Common.vaildDataType.filenames.datatype,
                nullmsg: Common.vaildDataType.filenames.nullmsg,
                errormsg: Common.vaildDataType.filenames.errormsg
            }])
        },
        shareFile: function() {
            var _Form = $(".shareFliesHasClassForm").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.dropBox.shareFile(data)
                }
            });
            _Form.addRule([{},
            {
                ele: ":checkbox:last",
                datatype: "*",
                nullmsg: "请选择班级",
                errormsg: "选择错误！"
            }])
        },
        resetFile: function() {
            var _Form = $(".resetFileNameForm").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.dropBox.resetFile(data)
                }
            });
            _Form.addRule([{
                ele: "#resetFileNameTxt",
                datatype: Common.vaildDataType.resetFiles.datatype,
                nullmsg: Common.vaildDataType.resetFiles.nullmsg,
                errormsg: Common.vaildDataType.resetFiles.errormsg
            }])
        }
    },
    classManage: {
        creat: function() {
            var _Form = $(".creatNewClassForm").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.classManage.creat(data)
                }
            });
            _Form.addRule([{
                ele: ".new_class_text",
                datatype: Common.vaildDataType.Classname.datatype,
                nullmsg: Common.vaildDataType.Classname.nullmsg,
                errormsg: Common.vaildDataType.Classname.errormsg
            },
            {
                ele: "select",
                datatype: "*",
                nullmsg: "请选择授课科目",
                errormsg: "选择错误！"
            }])
        },
        invite: function() {
            var _Form = $(".inVislbleClassForm").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.classManage.invite(data)
                }
            });
            _Form.addRule([{
                ele: ".in_vislble_class_text",
                datatype: Common.vaildDataType.Classnum.datatype,
                nullmsg: Common.vaildDataType.Classnum.nullmsg,
                errormsg: Common.vaildDataType.Classnum.errormsg
            }])
        },
        deleteClass: function(class_id) {
            var _Form = $(".deleteClassPopForm").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                beforeSubmit: function(curform) {
                    $(".classId").val(class_id),
                    Common.comValidform.md5(curform)
                },
                callback: function(data) {
                    CommonAjax.classManage.deleteClass(data)
                }
            });
            _Form.addRule([{
                ele: ".in_vislble_class_text",
                datatype: Common.vaildDataType.Passwd.datatype,
                nullmsg: Common.vaildDataType.Passwd.nullmsg,
                errormsg: Common.vaildDataType.Passwd.errormsg
            }])
        },
        createStudent: function() {
            var _Form = $(".createStudentCountPopForm").Validform({
                tiptype: function(msg, o) {
                    if (!o.obj.is("form")) {
                        var objtip = o.obj.next().find(".Validform_checktip");
                        objtip.text(msg),
                        o.obj.next().show();
                        var objtip = o.obj.next().find(".Validform_checktip");
                        objtip.text(msg);
                        var infoObj = o.obj.next(".ValidformTips");
                        2 == o.type && (infoObj.show(), o.obj.next().hide())
                    }
                },
                showAllError: !1,
                ajaxPost: !0,
                beforeSubmit: function(curform) {
                    $(".classId").val($("#class_id").val()),
                    Common.comValidform.md5(curform)
                },
                callback: function(data) {
                    CommonAjax.classManage.createStudent(data)
                }
            });
            _Form.addRule([{
                ele: ".studentNames",
                datatype: "*",
                nullmsg: "请输入姓名",
                errormsg: "至少需要输入一个姓名！"
            }])
        },
        createStudent_new: function() {
            var _Form = $(".createStudentName").Validform({
                tiptype: function(msg, o) {
                    if (!o.obj.is("form")) {
                        var objtip = o.obj.next().find(".Validform_checktip");
                        objtip.text(msg),
                        o.obj.next().show();
                        var objtip = o.obj.next().find(".Validform_checktip");
                        objtip.text(msg);
                        var infoObj = o.obj.next(".ValidformTips");
                        2 == o.type && (infoObj.show(), o.obj.next().hide())
                    }
                },
                showAllError: !1,
                ajaxPost: !0,
                beforeSubmit: function(curform) {
                    $(".classId").val($("#class_id").val()),
                    Common.comValidform.md5(curform)
                },
                callback: function(data) {
                    CommonAjax.classManage.createStudent(data)
                }
            });
            _Form.addRule([{
                ele: ".studentNames_new",
                datatype: "*",
                nullmsg: "请输入姓名",
                errormsg: "至少需要输入一个姓名！"
            }])
        },
        createStudentHasCount: function() {
            var _Form = $(".createStudentHasCount").Validform({
                tiptype: function(msg, o) {
                    if (!o.obj.is("form")) {
                        var objtip = o.obj.next().find(".Validform_checktip");
                        objtip.text(msg),
                        o.obj.next().show();
                        var objtip = o.obj.next().find(".Validform_checktip");
                        objtip.text(msg);
                        var infoObj = o.obj.next(".ValidformTips");
                        2 == o.type && (infoObj.show(), o.obj.next().hide())
                    }
                },
                showAllError: !1,
                ajaxPost: !0,
                beforeSubmit: function(curform) {
                    $(".classId").val($("#class_id").val()),
                    Common.comValidform.md5(curform)
                },
                callback: function(data) {
                    CommonAjax.classManage.createStudent(data)
                }
            });
            _Form.addRule([{
                ele: ".stuAccName",
                datatype: "*",
                nullmsg: "请输入账号",
                errormsg: "请输入账号！"
            }])
        },
        alterClassGrade: function() {
            var _Form = $(".alterClassGradePopForm").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                beforeSubmit: function() {
                    $(".classId").val($("#class_id").val())
                },
                callback: function(data) {
                    CommonAjax.classManage.alterClassGrade(data)
                }
            });
            _Form.addRule([{
                ele: ".newClassGrade",
                datatype: Common.vaildDataType.Classname.datatype,
                nullmsg: Common.vaildDataType.Classname.nullmsg,
                errormsg: Common.vaildDataType.Classname.errormsg
            }])
        }
    },
    uploadValid: {
        init: function() {
            this.mydocumentPerfect()
        },
        mydocumentPerfect: function() {
            var _Form = $(".form_fileList").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                beforeSubmit: function() {},
                callback: function(data) {
                    CommonAjax.uploadDoc.mydocumentPerfect(data)
                }
            });
            _Form.addRule([{
                ele: ".s_grade",
                datatype: "*",
                nullmsg: "请选择学段"
            },
            {
                ele: ".s_subject",
                datatype: "*",
                nullmsg: "请选择学科"
            },
            {
                ele: ".s_type",
                datatype: "*",
                nullmsg: "请选择文档类型"
            },
            {
                ele: ".sourceName",
                datatype: "*",
                nullmsg: "请输入文档名称"
            }])
        }
    },
    uploadQuesValid: {
        init: function() {
            this.myQuestion()
        },
        myQuestion: function() {
            var _Form = $(".form_qList").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                beforeSubmit: function() {},
                callback: function(data) {
                    CommonAjax.uploadQues.myQuestion(data)
                }
            });
            _Form.addRule([{
                ele: ".s_grade",
                datatype: "*",
                nullmsg: "请选择学段"
            },
            {
                ele: ".s_subject",
                datatype: "*",
                nullmsg: "请选择学科"
            },
            {
                ele: ".s_qtype",
                datatype: "*",
                nullmsg: "请选择题型"
            },
            {
                ele: ".source",
                datatype: "*",
                nullmsg: "请输入题目/来源"
            }])
        },
        myGroupTipForm: function(obj) {
            var _Form = $(".myGroupTipForm").Validform({
                tiptype: 3,
                showAllError: !1,
                ajaxPost: !0,
                beforeSubmit: function() {},
                callback: function(data) {
                    CommonAjax.uploadQues.myGroupTipForm(data, obj)
                }
            });
            _Form.config({
                tiptype: 3
            }),
            _Form.addRule([{
                ele: ".s_newSubject",
                datatype: "*",
                nullmsg: "请选择学科"
            },
            {
                ele: ".s_GroupName",
                datatype: "*1-20",
                nullmsg: "请选择分组名称",
                errormsg: "长度1-20字符之间"
            }])
        }
    },
    student: {
        modifyInfo: function() {
            var _Form = $(".stuInfoForm").Validform({
                tiptype: 3,
                showAllError: !1,
                beforeSubmit: function() {},
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.modifyInfo.modifyStuDetail(data)
                }
            });
            _Form.addRule([{
                ele: ".stuInfoTrueName",
                datatype: Common.vaildDataType.URname.datatype,
                nullmsg: Common.vaildDataType.URname.nullmsg,
                errormsg: Common.vaildDataType.URname.errormsg
            },
            {
                ele: ":radio:first",
                datatype: Common.vaildDataType.radioValid.datatype,
                nullmsg: "请选择性别",
                errormsg: "性别未选择！"
            },
            {
                ele: ".sel_mygrade",
                datatype: "*",
                nullmsg: "请选择年级",
                errormsg: "年级未选择！"
            },
            {
                ele: ".QQText",
                datatype: Common.vaildDataType.QQ.datatype,
                errormsg: Common.vaildDataType.QQ.errormsg
            }])
        },
        dropoutClass: function() {
            var _Form = $(".dropoutClassForm").Validform({
                tiptype: 3,
                showAllError: !1,
                beforeSubmit: function(curform) {
                    Common.comValidform.md5(curform)
                },
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.modifyInfo.dropoutClass(data)
                }
            });
            _Form.addRule([{
                ele: ".password",
                datatype: Common.vaildDataType.Passwd.datatype,
                nullmsg: Common.vaildDataType.Passwd.nullmsg,
                errormsg: Common.vaildDataType.Passwd.errormsg
            }])
        },
        addNewClassWin: function() {
            var _Form = $(".addNewClassWinForm").Validform({
                tiptype: 3,
                showAllError: !1,
                beforeSubmit: function() {},
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.modifyInfo.addNewClass(data)
                }
            });
            _Form.addRule([{
                ele: ".in_vislble_class_text",
                datatype: Common.vaildDataType.Classnum.datatype,
                nullmsg: Common.vaildDataType.Classnum.nullmsg,
                errormsg: Common.vaildDataType.Classnum.errormsg
            }])
        },
        complaint: function() {
            var _Form = $(".complaintConentForm").Validform({
                tiptype: 3,
                showAllError: !1,
                beforeSubmit: function() {
                    var checkcode = $(".textCaptcha").val();
                    return Common.comValidform.checkCaptcha(checkcode, 1)
                },
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.Aq.complaint(data)
                }
            });
            _Form.addRule([{
                ele: ".contentTextarea",
                datatype: "*5-1000",
                nullmsg: "请填写投诉内容！",
                errormsg: "投诉内容5-1000个字符之间！"
            },
            {
                ele: ".tel",
                datatype: "m | /^\\w{0}$/",
                errormsg: Common.vaildDataType.Phone.errormsg
            },
            {
                ele: ".textCaptcha",
                datatype: Common.vaildDataType.CaptchaCode.datatype,
                nullmsg: Common.vaildDataType.CaptchaCode.nullmsg,
                errormsg: Common.vaildDataType.CaptchaCode.errormsg
            }])
        }
    },
    parent: {
        modifyInfo: function() {
            var _Form = $(".parentInfoForm").Validform({
                tiptype: 3,
                showAllError: !1,
                beforeSubmit: function() {
                    var b_year = $(".b_year option:selected"),
                    b_month = $(".b_month option:selected"),
                    b_day = $(".b_day option:selected"),
                    text = "请正确填写生日";
                    return $(".Validform_checktip").eq(2).html("").removeClass().addClass("Validform_checktip fl"),
                    0 == b_year.val() && 0 == b_month.val() && 0 == b_day.val() ? ($(".Validform_checktip").eq(2).html("&nbsp;").addClass("Validform_right"), !0) : 0 == b_year.val() || 0 == b_month.val() || 0 == b_day.val() ? ($(".Validform_checktip").eq(2).html(text).addClass("Validform_wrong"), !1) : ($(".Validform_checktip").eq(2).html("&nbsp;").addClass("Validform_right"), !0)
                },
                ajaxPost: !0,
                callback: function(data) {
                    CommonAjax.modifyInfo.modifyParentDetail(data)
                }
            });
            _Form.addRule([{
                ele: ".parentInfoTrueName",
                datatype: Common.vaildDataType.URname.datatype,
                nullmsg: Common.vaildDataType.URname.nullmsg,
                errormsg: Common.vaildDataType.URname.errormsg
            },
            {
                ele: ":radio:first",
                datatype: Common.vaildDataType.radioValid.datatype,
                nullmsg: "请选择性别",
                errormsg: "性别未选择！"
            }])
        }
    }
},
Common.sendCaptap = {
    sms: {
        init: function(phone, code_type, element) {
            var _this = this;
            $(".aSendCaptap").removeAttr("disabled"),
            $(".aSendCaptap").live("click",
            function() {
                element && (phone = $(element).val()),
                Common.comValidform.sendPhoneCode(phone, code_type,
                function(data) {
                    _this.telWaitTime(phone, code_type),
                    $(".sendTelCaptap .Validform_checktip").removeClass("Validform_wrong").addClass("Validform_right").html(data.error),
                    $(this).addClass("aSendCaptapOk"),
                    $(".aSendCaptap").attr("disabled", !0)
                },
                function(data) {
                    $(".sendTelCaptap .Validform_checktip").removeClass("Validform_right").addClass("Validform_wrong").html(data.error)
                })
            })
        },
        telWaitTime: function() {
            var wait = 90,
            timer = setInterval(function() {
                wait--,
                0 > wait ? (clearInterval(timer), $(".aSendCaptap").removeClass("aSendCaptapOk").removeAttr("disabled").val("发送验证码"), $(".aSendCaptap").next("span").fadeOut()) : $(".aSendCaptap").val(wait + "秒后重新发送")
            },
            1e3)
        }
    },
    email: {
        init: function(email, code_type) {
            var _this = this;
            $(".aSendEmail").removeAttr("disabled"),
            $(".aSendEmail").live("click",
            function() {
                $(this).parent().parent().parent().siblings("dd").removeClass("activeDiv"),
                Common.comValidform.sendEmailCode(email, code_type,
                function() {
                    _this.emailWaitTime(email, code_type),
                    $(this).addClass("aSendCaptapOk"),
                    $(".aSendEmail").attr("disabled", !0)
                },
                function(data) {
                    $(".aSendEmailTip").html(data.error).addClass("error").removeClass("undis")
                })
            })
        },
        emailWaitTime: function() {
            var wait = 90,
            timer = setInterval(function() {
                wait--,
                0 > wait ? (clearInterval(timer), $(".aSendEmail").removeClass("aSendEmailOk").removeAttr("disabled").val("重新发送"), $(".aSendEmail").next("span").fadeOut()) : $(".aSendEmail").val(wait + "秒后重新发送")
            },
            1e3)
        }
    }
},
Common.small = {
    regSuccessSelect: function() {
        $(".regRoleSuccess li a").on("click",
        function() {
            $(".regRoleSuccess li a").removeClass("active"),
            $(this).addClass("active")
        })
    }
},
Common.navSlidown = function() {
    $("#navSlidown").hover(function() {
        $(this).find("span").addClass("down"),
        $(this).find("p").removeClass("undis")
    },
    function() {
        $(this).find("span").removeClass("down"),
        $(this).find("p").addClass("undis")
    })
},
Common.navSlidown(),
Common.LoadScript = function(url, callback) {
    var f = arguments.callee;
    "queue" in f || (f.queue = {});
    var queue = f.queue;
    if (url in queue) return callback && (queue[url] ? queue[url].push(callback) : callback()),
    void 0;
    queue[url] = callback ? [callback] : [];
    var script = document.createElement("script");
    script.type = "text/javascript",
    script.onload = script.onreadystatechange = function() {
        if (!script.readyState || "loaded" == script.readyState || "complete" == script.readyState) {
            for (script.onreadystatechange = script.onload = null; queue[url].length;) queue[url].shift()();
            queue[url] = null
        }
    },
    script.src = url,
    document.getElementsByTagName("head")[0].appendChild(script)
},
Common.floatLoading = {
    showLoading: function() {},
    hideLoading: function() {},
    showFloatLoading: function() {
        $(".floatLoadingCover").length < 1 && $("body").append('<div class="floatLoadingCover"></div><div class="cFloatLoadingGif"><span></span><p class="return undis"><a href="javascript:void(0)">返回上页</a></p></div>'),
        $(".return").hide(),
        this.loadingDivShow(),
        this.returnPre()
    },
    hideFloatLoading: function() {
        $(".floatLoadingCover").hide(),
        $(".cFloatLoadingGif").hide(),
        clearInterval(this.timers),
        clearInterval(this.timerID),
        $(".return").hide()
    },
    loadingDivShow: function() {
        $(".floatLoadingCover").css("height", $(window).height()).show(),
        $(".cFloatLoadingGif").show()
    },
    returnPre: function() {
        $(".return").show(),
        $(".return").click(function() {
            Common.floatLoading.hideFloatLoading()
        })
    }
},
Common.Overlay = {
    show: function(Dialog) { ("" != Dialog || void 0 != Dialog) && $(Dialog).addClass("cover_dialog"),
        $(".cover_layer").length < 1 && ($("body").append('<div class="cover_layer"></div>'), $("body").append('<iframe rameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" class="cover_layer" style="display:none"></iframe>'), $(".cover_layer").height($(document).height()).show()),
        $(window).resize(function() {
            $(".cover_layer").length > 0 && $(".cover_layer").height($(document).height())
        })
    },
    hide: function(Dialog) { ("" != Dialog || void 0 != Dialog) && $(Dialog).removeClass("cover_dialog"),
        $(".cover_dialog").length <= 1 && $(".cover_layer").remove()
    },
    fadeOut: function(Dialog) { ("" != Dialog || void 0 != Dialog) && $(Dialog).removeClass("cover_dialog"),
        $(".cover_dialog").length <= 1 && $(".cover_layer").fadeOut(function() {
            $(".cover_layer").remove()
        })
    }
},
Common.Msg = {
    ErrorMsg: $("#errormsg"),
    errormsg: function() {
        void 0 != this.ErrorMsg.html() && "" != this.ErrorMsg.html() && $.tiziDialog({
            content: this.ErrorMsg.html()
        })
    }
},
Common.Msg.errormsg(),
Common.Download = {
    force_download: function(url, fname, openbox, noxunlei) {
        noxunlei || (url = url + "&session_id=" + $.cookies.get(baseSessID));
        var ie_ver = this.ie_version();
        return 1 == openbox || 6 == ie_ver || 7 == ie_ver || 8 == ie_ver ? (fname = "" == fname || void 0 == fname ? "是否下载？": "是否下载《" + fname + "》？", $.tiziDialog({
            content: fname,
            ok: !1,
            icon: null,
            button: [{
                name: "点击下载",
                href: url,
                className: "aui_state_highlight",
                target: "_self"
            }]
        }), !1) : (window.location.href = url, void 0)
    },
    ie_version: function() {
        var ie = $.browser.msie,
        version = $.browser.version;
        return ie ? version: !1
    }
},
Common.tips = {
    flag: !0,
    status: function(tips_name, with_cookie) {
        void 0 == with_cookie && (with_cookie = 0);
        var _this = this;
        $.ajax({
            type: "GET",
            dataType: "json",
            url: baseUrlName + "login/tips/get_tips",
            data: {
                tips_name: tips_name,
                with_cookie: with_cookie,
                ver: (new Date).valueOf()
            },
            success: function(data) {
                data.status === !1 && _this.show()
            }
        })
    },
    close: function(tips_name, with_cookie) {
        void 0 == with_cookie && (with_cookie = 0),
        $.tizi_ajax({
            type: "POST",
            dataType: "json",
            url: baseUrlName + "login/tips/close_tips",
            data: {
                tips_name: tips_name,
                with_cookie: with_cookie,
                ver: (new Date).valueOf()
            },
            success: function() {}
        })
    },
    show: function() {
        this.thisPage()
    },
    thisPage: function() {
        {
            var _action = $(".mainContainer").attr("status") || $(".preview-title").attr("status") || $(".reg-container").attr("status");
            $(".aReturnParent").length
        }
        switch (_action) {
        case "paper_question":
            this.survey()
        }
    },
    survey: function() {
        $.tiziDialog({
            title: "梯子网调查问卷",
            icon: "survey",
            width: 450,
            content: "为了给您提供更周到的服务，我们敬请您参与梯子网用户调查问卷，感谢您在百忙之中接受我们的调查，谢谢您的参与和支持！",
            ok: !1,
            button: [{
                name: "参与调查",
                href: "http://www.diaochapai.com/survey868801/",
                target: "_blank",
                className: "cBtnNormalOrg"
            }],
            close: function() {
                Common.tips.close("paper_question_survey")
            }
        })
    }
},
Common.tipsInfo = {
    status: function(tips_name, with_cookie) {
        void 0 == with_cookie && (with_cookie = 0);
        var _this = this;
        $.ajax({
            type: "GET",
            dataType: "json",
            url: baseUrlName + "login/tips/get_tips",
            data: {
                tips_name: tips_name,
                with_cookie: with_cookie,
                ver: (new Date).valueOf()
            },
            success: function(data) {
                data.status === !1 && _this.show()
            }
        })
    },
    close: function(tips_name, with_cookie) {
        void 0 == with_cookie && (with_cookie = 0),
        $.tizi_ajax({
            type: "POST",
            dataType: "json",
            url: baseUrlName + "login/tips/close_tips",
            data: {
                tips_name: tips_name,
                with_cookie: with_cookie,
                ver: (new Date).valueOf()
            },
            success: function() {}
        })
    },
    show: function() {
        var _action = $(".mainContainer").attr("status") || $(".preview-title").attr("status") || $(".reg-container").attr("status");
        switch (_action) {
        case "paper_question":
            $(".paperQuestionTips").show(),
            $(".paperQuestionTips .close").click(function() {
                $(this).parent().hide(),
                Common.tipsInfo.close("paperQuestionTips")
            });
            break;
        case "lessonStatus":
            $(".lessonTips").show(),
            $(".lessonTips .close").click(function() {
                $(this).parent().hide(),
                Common.tipsInfo.close("lessonTips")
            });
            break;
        case "homeworkStatus":
            $(".homeworkQuestionTips").show(),
            $(".homeworkQuestionTips .close").click(function() {
                $(this).parent().hide(),
                Common.tipsInfo.close("homeworkQuestionTips")
            })
        }
    }
},
Common.pagination = function(this_a) {
    $(this_a).parent().find("a").attr("onclick", "")
},
Common.calendar = {
    calendarDate: function(textId, popId) {
        var property = {
            divId: popId,
            needTime: !0,
            yearRange: [1970, 2030],
            week: ["日", "一", "二", "三", "四", "五", "六"],
            month: ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"],
            format: "yyyy-MM-dd hh时"
        };
        canva1 = $.createGooCalendar(textId, property)
    },
    calendarTime: function() {
        var d = new Date,
        vYear = d.getFullYear(),
        vMon = d.getMonth() + 1,
        vDay = d.getDate(),
        h = d.getHours(),
        m = d.getMinutes(),
        se = d.getSeconds();
        return s = vYear + "-" + (10 > vMon ? "0" + vMon: vMon) + "-" + (10 > vDay ? "0" + vDay: vDay) + "-" + (10 > h ? "0" + h: h) + "-" + (10 > m ? "0" + m: m) + "-" + (10 > se ? "0" + se: se)
    }
},
function($) {
    $.tizi_callback = function(data, success) {
        return void 0 != data ? data.login === !1 ? (window.location.href = baseUrlName, !1) : data.token === !1 ? (window.location.reload(), !1) : ("" != data.token && void 0 != data.token && $(".token").attr("id", data.token), void 0 == success && (success = function() {}), success(data), void 0) : void 0
    },
    $.tizi_token = function(options_data, type, serialize, callback_name) {
        if (serialize === !0) {
            var len = options_data.length;
            if (options_data[len] = {
                name: "ver",
                value: (new Date).valueOf()
            },
            "post" == type.toLocaleLowerCase() && (options_data[len + 1] = {
                name: "token",
                value: $(".token").attr("id")
            },
            options_data[len + 2] = {
                name: "page_name",
                value: $(".pname").attr("id")
            }), callback_name) {
                var len = options_data.length;
                options_data[len] = {
                    name: "callback_name",
                    value: callback_name
                }
            }
        } else options_data.ver = (new Date).valueOf(),
        "post" == type.toLocaleLowerCase() && (options_data.token = $(".token").attr("id"), options_data.page_name = $(".pname").attr("id")),
        callback_name && (options_data.callback_name = callback_name);
        return options_data
    },
    $.tizi_ajax = function(options, serialize) {
        var defaults = {
            url: "",
            type: "POST",
            data: {},
            dataType: "json",
            async: !0,
            success: function() {},
            error: function() {}
        },
        options = $.extend(defaults, options);
        if ("jsonp" == options.dataType) {
            options.jsonp = "callback",
            options.data = $.tizi_token(options.data, options.type, serialize, options.jsonp);
            var success = options.success;
            callback = function(data) {
                $.tizi_callback(data, success)
            },
            options.success = function() {},
            options.error = function() {}
        } else {
            options.dataType = "json",
            options.data = $.tizi_token(options.data, options.type, serialize);
            var success = options.success;
            options.success = function(data) {
                $.tizi_callback(data, success)
            }
        }
        $.ajax(options)
    }
} (jQuery),
Common.Notice = {
    init: function() {},
    page: function(page, url_type) {
        this.randerPage(page, url_type)
    },
    randerPage: function(page, url_type) { ("" == page || null == page || void 0 == page) && (page = ""),
        content = "",
        $.tizi_ajax({
            type: "GET",
            dataType: "json",
            url: baseUrlName + "user/user_" + url_type + "/notification",
            data: {
                rf: !0,
                page: page,
                ver: (new Date).valueOf()
            },
            success: function(data) {
                data.errorcode ? ($(".notice_list").html(data.html), "teacher" == url_type && Common.Notice.autoHeight()) : $.tiziDialog({
                    content: data.error
                })
            }
        })
    },
    autoHeight: function() {
        $(".noticeBox").height($(window).height() - 130),
        $(".myNoticeList").height($(window).height() - 130).css("overflow-y", "auto")
    },
    getNotifyNews: function() {
        $.tizi_ajax({
            type: "GET",
            url: loginUrlName + "notice",
            dataType: "jsonp",
            success: function(data) {
                99 == data.status && (data.msg > 0 ? ($("#notification").addClass("notification"), $(".header .noticeMsg").html("<i></i>通知(" + data.msg + ")"), $("#notification").css("display", "inline-block")) : ($("#notification").css("display", "none"), $("#notification").removeClass("notification"), $(".header .noticeMsg").html("")))
            }
        })
    },
    setNextNews: function() {
        setTimeout("Common.Notice.getNotifyNews()", 12e4)
    }
},
Common.Notice.getNotifyNews(),
Common.Feedback = {
    flag: !1,
    init: function() {
        $(".cBtnFeedback").click(function(event) {
            event.preventDefault(),
            Common.Feedback.dialog()
        })
    },
    dialog: function() {
        $.tiziDialog({
            id: "feedbackFormID",
            title: "用户反馈",
            content: $(".includeFeedback").html().replace("feedbackForm1", "feedbackForm"),
            icon: null,
            ok: !1,
            width: 680,
            height: 390,
            init: function() {
                Common.LoadScript("http://wpa.b.qq.com/cgi/wpa.php?" + (new Date).valueOf(),
                function() {
                    function BizQQFeedback() {
                        void 0 != window.BizQQWPA && BizQQWPA.add({
                            aty: "0",
                            type: "10",
                            fsty: "1",
                            fposX: "2",
                            fposY: "2",
                            ws: "www.tizi.com",
                            nameAccount: "800068391",
                            parent: "qq2"
                        })
                    }
                    BizQQFeedback()
                })
            },
            button: [{
                name: "确定",
                className: "aui_state_highlight",
                focus: !1,
                callback: function() {
                    return $(".feedbackForm").submit(),
                    !1
                }
            },
            {
                name: "取消"
            }],
            close: function() {
                var qq_tip_about = $(".qq_tip_about");
                qq_tip_about.length > -1 ? qq_tip_about.show() : ""
            }
        });
        var qq_tip_about = $(".qq_tip_about");
        qq_tip_about.length > -1 ? qq_tip_about.hide() : "",
        Common.comValidform.changeCaptcha("feedbackBox", ".changeCaptchaFB"),
        Common.comValidform.bindChangeVerify("feedbackBox", ".changeCaptchaFB"),
        Common.Feedback.FeedbackCheck()
    },
    FeedbackCheck: function() {
        var varFeedbackCheck = $(".feedbackForm").Validform({
            tiptype: 3,
            showAllError: !0,
            ajaxPost: !0,
            beforeSubmit: function() {
                var checkcode = $(".textCaptcha").val();
                return Common.comValidform.checkCaptcha(checkcode, 1, 0, "feedbackBox")
            },
            callback: function(data) {
                CommonAjax.feedback.submit(data)
            }
        });
        varFeedbackCheck.addRule([{
            ele: ".contentTextarea",
            datatype: "*5-1000",
            nullmsg: "请填写反馈内容！",
            errormsg: "反馈内容5-1000个字符之间！"
        },
        {
            ele: ".QQText",
            datatype: Common.vaildDataType.QQ.datatype,
            errormsg: Common.vaildDataType.QQ.errormsg
        },
        {
            ele: ".QQTextUnlogin",
            datatype: Common.vaildDataType.QQ.datatype_nonull,
            nullmsg: Common.vaildDataType.QQ.nullmsg,
            errormsg: Common.vaildDataType.QQ.errormsg
        },
        {
            ele: ".textCaptcha",
            datatype: Common.vaildDataType.CaptchaCode.datatype,
            nullmsg: Common.vaildDataType.CaptchaCode.nullmsg,
            errormsg: Common.vaildDataType.CaptchaCode.errormsg
        }])
    }
},
Common.Feedback.init(),
Common.Switch = {
    init: function() {
        $(".toStudent").live("click",
        function() {
            Common.Switch.toStudent()
        }),
        $(".toParent").live("click",
        function() {
            Common.Switch.toParent()
        })
    },
    toParent: function() {
        $.tizi_ajax({
            url: baseUrlName + "user/switch/parent",
            type: "get",
            dataType: "json",
            success: function(data) {
                data.errorcode ? data.redirect && (window.location.href = data.redirect) : data.bind && ($.tiziDialog({
                    title: "绑定家长账号",
                    content: data.html,
                    icon: null,
                    width: 380,
                    ok: function() {
                        return $(".regParentForm").submit(),
                        !1
                    },
                    cancel: !0
                }), Common.valid.init(), Common.comValidform.changeCaptcha("RegPBox", ".changeCaptchaRegP"), Common.comValidform.bindChangeVerify("RegPBox", ".changeCaptchaRegP"))
            }
        })
    },
    toStudent: function() {
        $.tizi_ajax({
            url: baseUrlName + "user/switch/student",
            type: "get",
            dataType: "json",
            success: function(data) {
                data.errorcode ? data.redirect && (window.location.href = data.redirect) : data.bind && ($.tiziDialog({
                    title: "绑定学生账号",
                    content: data.html,
                    icon: null,
                    width: 380,
                    ok: function() {
                        return $(".regStudentForm").submit(),
                        !1
                    },
                    cancel: !0
                }), Common.valid.init(), Common.comValidform.changeCaptcha("RegSBox", ".changeCaptchaRegS"), Common.comValidform.bindChangeVerify("RegSBox", ".changeCaptchaRegS"))
            }
        })
    }
},
Common.Switch.init();