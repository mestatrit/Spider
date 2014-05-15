var CommonAjax = {};
CommonAjax.feedback = {
    submit: function(data) {
        data.errorcode ? ($.tiziDialog.list.feedbackFormID.close(), $.tiziDialog({
            content: data.error,
            time: 3,
            lock: !0,
            icon: "succeed"
        })) : $.tiziDialog({
            content: data.error,
            time: 3,
            lock: !0,
            icon: "error"
        })
    }
},
CommonAjax.Login = {
    submit: function(data) {
        data.errorcode ? data.redirect && (window.location.href = data.redirect) : $.tiziDialog({
            content: data.error
        })
    }
},
CommonAjax.Register = {
    submit: function(data, captcha_name, element) {
        data.errorcode ? data.redirect ? data.switch_user && -1 != data.redirect.indexOf(window.location.href + "#") ? (window.location.href = data.redirect, window.location.reload()) : window.location.href = data.redirect: ($.tiziDialog.list.regStudentFormID && $.tiziDialog.list.regStudentFormID.close(), data.kid_urname && ($(".myAccount").find("p").html(data.kid_urname), $(".addNewClass").find("h3").html("你好，" + data.kid_urname + "！"), $("#addNewClass").click())) : (Common.comValidform.changeCaptcha(captcha_name, element), $.tiziDialog({
            content: data.error
        }))
    },
    mysubject: function(data) {
        data.errorcode ? data.redirect ? window.location.href = data.redirect: ($(".mySubjects").length > 0 && ($(".mySubjects dt p span").html($(".mySubjects a.active").parent().prev("span").find("em").html() + $(".mySubjects a.active").text()), $(".mySubjects").find("dd").removeClass("activeDiv")), $.tiziDialog({
            content: data.error
        })) : $.tiziDialog({
            content: data.error
        })
    },
    mygrade: function(data) {
        data.errorcode ? data.redirect ? window.location.href = data.redirect: $.tiziDialog({
            content: data.error
        }) : $.tiziDialog({
            content: data.error
        })
    },
    myuname: function(data) {
        data.errorcode ? data.redirect ? window.location.href = data.redirect: $.tiziDialog({
            content: data.error
        }) : $.tiziDialog({
            content: data.error
        })
    },
    perfect_teacher: function(data) {
        1 == data.code ? window.location.href = data.redirect: $.tiziDialog({
            content: data.msg
        })
    }
},
CommonAjax.findPassword = {
    reset_mode: function(data) {
        if (data.errorcode) switch ($(".form_div").html(data.html), data.utype) {
        case 1:
            Common.sendCaptap.email.init(data.email, data.code_type);
            break;
        case 2:
            Common.sendCaptap.sms.init(data.phone, data.code_type),
            Common.valid.forgetPwTel();
            break;
        case 3:
        case 4:
            Common.valid.findPwOnlyTel()
        } else Common.comValidform.changeCaptcha("ForgotBox", ".changeCaptchaFG"),
        $.tiziDialog({
            content: data.error
        })
    },
    reset: function(data) {
        data.errorcode ? ($(".Container").html(data.html), Common.valid.restPassword()) : $.tiziDialog({
            content: data.error
        })
    },
    apply: function(data) {
        data.errorcode ? $(".Container").html(data.html) : $.tiziDialog({
            content: data.error
        })
    },
    submit: function(data) {
        data.errorcode ? $(".Container").html(data.html) : $.tiziDialog({
            content: data.error
        })
    }
},
CommonAjax.dropBox = {
    creat: function(data) {
        1 == data.errorcode ? ($.tiziDialog.list.create_new_fileId.close(), $.tiziDialog({
            content: data.error,
            icon: "succeed",
            ok: function() {}
        }), window.location.reload()) : $.tiziDialog({
            content: data.error
        })
    },
    shareFile: function(data) {
        1 == data.errorcode ? ($.tiziDialog.list.shareFile_id.close(), $.tiziDialog({
            content: data.error,
            icon: "succeed",
            ok: function() {}
        })) : $.tiziDialog({
            content: data.error
        })
    },
    resetFile: function(data) {
        if (1 == data.errorcode) {
            $.tiziDialog.list.resetFile_id.close();
            var ext = $("tr[df_id=" + data.file_id + "]").find("span.for_rename").attr("file-ext");
            ext = ext ? "." + ext: "";
            var is_file = $("tr[df_id=" + data.file_id + "]").find("a.resetFlieName").attr("is_file"),
            name = data.name;
            is_file && (name = data.name + ext),
            $("tr[df_id=" + data.file_id + "]").find("span.for_rename").text(name),
            $("tr[df_id=" + data.file_id + "]").find("a").attr("file_name", name),
            $("tr[df_id=" + data.file_id + "]").find("a.resetFlieName").attr("file_name", data.name),
            $.tiziDialog({
                content: data.error,
                icon: "succeed",
                ok: function() {}
            })
        } else $.tiziDialog({
            content: data.error
        })
    }
},
CommonAjax.classManage = {
    creat: function(data) {
        1 == data.code ? ($.tiziDialog.list.create_new_classId.close(), $.tiziDialog({
            content: data.msg,
            icon: "succeed",
            ok: function() {
                window.location.href = data.redirect
            }
        })) : -999 == data.code ? Teacher.NewClassManage.show_login() : $.tiziDialog({
            content: data.msg
        })
    },
    invite: function(data) {
        1 == data.code ? window.location.href = data.redirect: -999 == data.code ? Teacher.NewClassManage.show_login() : $.tiziDialog({
            content: data.msg
        })
    },
    deleteClass: function(data) {
        1 == data.code ? ($.tiziDialog.list.deleteClassId.close(), $.tiziDialog({
            content: data.msg,
            icon: "succeed",
            ok: function() {
                window.location.reload()
            }
        })) : -999 == data.code ? Teacher.NewClassManage.show_login() : $.tiziDialog({
            content: data.msg
        })
    },
    createStudent: function(data) {
        if (1 == data.code) {
            var st = data.students;
            $.tiziDialog({
                content: data.msg + "<br />请将学号和密码告知您的学生，尽快登录。",
                icon: "succeed",
                ok: function() {
                    for (var tr = "",
                    i = 0; i < data.students.length; ++i) {
                        var atr = $("#crtemplate").html();
                        atr = atr.replace("%student_name%", st[i].student_name),
                        atr = atr.replace("%student_id%", st[i].student_id),
                        atr = atr.replace("%student_id%", st[i].student_id),
                        atr = atr.replace("%password%", st[i].password),
                        tr += atr
                    }
                    $.browser.msie && "7.0" == $.browser.version || $.browser.msie && "6.0" == $.browser.version ? ($(".myallClass_student").append("<tr>" + tr + "</tr>"), $(".myallClass_student tr").each(function() {
                        "" == $(this).html() && $(this).remove()
                    })) : $(".myallClass_student").append(tr);
                    var student_total = parseInt($("#student_total").text());
                    $("#student_total").text(student_total + data.students.length),
                    $("#invi").css("display", "inline-block")
                }
            })
        } else - 999 == data.code ? Teacher.NewClassManage.show_login() : $.tiziDialog({
            content: data.msg
        })
    },
    teacherInputStudent: function(data) {
        if (1 == data.code) {
            {
                data.students
            }
            $.tiziDialog({
                content: data.msg,
                icon: "succeed",
                ok: function() {
                    1 == data.code && (window.location.href = window.location.href)
                }
            })
        } else - 999 == data.code ? Teacher.NewClassManage.show_login() : $.tiziDialog({
            content: data.msg
        })
    },
    alterClassGrade: function(data) {
        1 == data.code ? $.tiziDialog({
            content: data.msg,
            icon: "succeed",
            ok: function() {
                var _name = data.name;
                $("#ClassGrade").text(_name),
                $("#lft_" + $("#class_id").val()).text(_name)
            }
        }) : -999 == data.code ? Teacher.NewClassManage.show_login() : $.tiziDialog({
            content: data.msg
        })
    }
},
CommonAjax.modifyInfo = {
    modifyMyName: function(data) {
        data.errorcode ? ($(".myTrueName").find("dd").removeClass("activeDiv"), $.tiziDialog({
            content: data.error
        }), $(".myTrueName").find("dt p span").html($(".modifyUserName").val()), $("#tiziUrname").html($(".modifyUserName").val())) : $.tiziDialog({
            content: data.error
        })
    },
    modifyEmail: function(data) {
        if (data.errorcode) {
            $(".myEmail").find("dd").removeClass("activeDiv");
            var html = '<span class="bindPhoneInfo">' + $(".modifyEmail").val() + '<em>未验证</em>（<input type="button" class="aSendEmail aSendEmailInput" data-mail_address="' + $(".modifyEmail").val() + '" value="发送验证邮件" /><span class="aSendEmailTip"></span>）</span>';
            $(".bindEmailInfo").html(html),
            Common.sendCaptap.email.init($(".modifyEmail").val(), 3),
            $.tiziDialog({
                content: data.error
            })
        } else $.tiziDialog({
            content: data.error
        })
    },
    modifyPhone: function(data) {
        if (data.errorcode) if ($.tiziDialog.list.bindPhoneID) $.tiziDialog.list.bindPhoneID.close();
        else {
            $(".myPhone").find("dd").removeClass("activeDiv");
            var phone = $(".modifyPhone").val();
            phone = phone.substr(0, 3) + "****" + phone.substr(7, 10);
            var html = '<span class="bindPhoneInfo">' + phone + "</span>";
            $(".bindPhoneInfo").html(html),
            $.tiziDialog({
                content: data.error
            }),
            $(".modifyMyPhoneForm").hide()
        } else $.tiziDialog({
            content: data.error
        })
    },
    modifyMyPassword: function(data) {
        data.errorcode ? ($(".modifyPassword").find("dd").removeClass("activeDiv"), $.tiziDialog({
            content: data.error
        })) : $.tiziDialog({
            content: data.error
        })
    },
    modifyStuDetail: function(data) {
        if (data.errorcode) {
            var urname = $(".stuInfoTrueName").val();
            $("#tiziUrname").html(urname),
            $(".myAccount").find("p").html(urname),
            $.tiziDialog({
                content: data.error
            })
        } else $.tiziDialog({
            content: data.error
        })
    },
    dropoutClass: function(data) {
        1 == data.code ? ($(".myclass").find("span").html("暂无"), $(".stuInfo_li").find("span").html("暂无"), $.tiziDialog.list.studentDropoutClassID.close(), $.tiziDialog({
            content: data.msg,
            icon: "succeed"
        })) : $.tiziDialog({
            content: data.msg
        })
    },
    addNewClass: function(data) {
        if (1 == data.code) {
            $.tiziDialog.list.addNewClassWinID.close();
            var content = $("#addNewClassuccWin").html();
            content = content.replace("%classname%", data.classname),
            content = content.replace("%student_id%", data.student_id),
            $.tiziDialog({
                content: content,
                icon: "succeed",
                ok: function() {
                    window.location.reload()
                },
                close: function() {
                    window.location.reload()
                }
            })
        } else $.tiziDialog({
            content: data.msg
        })
    },
    modifyParentDetail: function(data) {
        if (data.errorcode) {
            var urname = $(".parentInfoTrueName").val();
            $("#tiziUrname").html(urname),
            $.tiziDialog({
                content: data.error
            })
        } else $.tiziDialog({
            content: data.error
        })
    }
},
CommonAjax.uploadDoc = {
    mydocumentPerfect: function(data) {
        data.error_code ? data.isequal ? top.location.href = baseUrlName + "teacher/user/mydocument/" + data.subject_id: this.mydocumentTip() : $.tiziDialog({
            content: data.error
        })
    },
    mydocumentTip: function() {
        var s_subject = $(".s_subject"),
        s_subjectText = "";
        s_subject.each(function() {
            s_subjectText += '<a href="' + baseUrlName + "teacher/user/mydocument/" + $(this).find("option:selected").val() + '">' + $(this).find("option:selected").text() + "</a>"
        }),
        $(".myDocumentTipList").html(s_subjectText),
        console.log($(".tmpDocTip").html()),
        $.tiziDialog({
            content: $(".tmpDocTip").html(),
            icon: null,
            ok: !1,
            cancel: !1
        })
    }
},
CommonAjax.uploadQues = {
    myQuestion: function(data) {
        if (data.error_code) {
            var type = data.type,
            title = "insert" == type ? "继续上传": "继续编辑";
            $.tiziDialog({
                content: data.error,
                okVal: title,
                cancelVal: "查看我的试题",
                ok: function() {
                    this.close(),
                    "insert" == type && (window.location.href = baseUrlName + "teacher/user/myquestion/new")
                },
                cancel: function() {
                    var optionId = $(".s_subject option:selected").val();
                    window.location.href = baseUrlName + "teacher/user/myquestion/" + optionId
                }
            })
        } else $.tiziDialog({
            content: data.error
        })
    },
    myGroupTipForm: function(data, obj) {
        var oldGroup = obj.parent().find(".s_Group");
        if (data.error_code) {
            var newGroupName = data.new_name,
            newGroupID = data.error,
            newOption = '<option value ="' + newGroupID + '" selected="selected">' + newGroupName + "</option>";
            oldGroup.removeAttr("selected"),
            oldGroup.append(newOption),
            $.tiziDialog.list.tmpGroupTipId.close()
        } else $.tiziDialog({
            content: data.error
        })
    }
},
CommonAjax.stuFirstLogin = {
    submit: function(json) {
        1 == json.code ? window.location.href = json.redirect: $.tiziDialog({
            content: json.msg
        })
    }
},
CommonAjax.Aq = {
    complaint: function(res) {
        res.errorcode ? (art.dialog.list.complaintId.close(), $.tiziDialog({
            content: "感谢您使用我们的产品！我们将会在3个工作日内处理您的投诉。",
            icon: "succeed",
            time: 2
        })) : $.tiziDialog({
            content: res.error,
            icon: null
        })
    },
    AqStart: function(data) {
        var json = data;
        json.error > 0 ? $.tiziDialog({
            icon: "succeed",
            content: "提问成功.",
            ok: function() {
                window.location.href = baseUrlName + "student/question/detail/" + json.error
            }
        }) : $.tiziDialog({
            content: json.error
        })
    }
},
CommonAjax.TeacherGroup = {
    AddDocForm: function(data) {
        var group_name = $(".group_name").val(),
        short_name = "";
        if (short_name = group_name.length > 9 ? group_name.substr(0, 9) + "..": group_name, 0 == data.error_code) $.tiziDialog({
            content: data.error
        });
        else {
            var doc_str = '<li><span class="group_txt"><a href="javascript:void(0);" title="' + group_name + '" class="filter-group" data-gid="' + data.error + '">' + short_name + '</a><b>(0)</b></span><a href="javascript:void(0);" data-group="' + data.error + '" class="fr del_doc">删</a><a href="javascript:void(0);" data-group="' + data.error + '" class="fr edit_doc">编</a></li>';
            $(".doc_group ul").append(doc_str),
            art.dialog.list.AddDocForm.close()
        }
    },
    EditDocForm: function(data, that) {
        var ele = that,
        group_name = $(".group_name").val(),
        short_name = "";
        short_name = group_name.length > 9 ? group_name.substr(0, 9) + "..": group_name,
        0 == data.error_code ? $.tiziDialog({
            content: data.error
        }) : (ele.parents("li").find(".group_txt").find("a").text(short_name), ele.parents("li").find(".group_txt").find("a").attr("title", group_name), art.dialog.list.EditDocForm.close())
    }
},
CommonAjax.paperHaveError = {
    addPaperHaveError: function(data) {
        if (0 == data.errorcode) $.tiziDialog({
            content: data.error
        });
        else {
            var content = "<p>纠错信息提交成功！</p><p>非常感谢您的反馈，我们会尽快处理。</p>";
            $.tiziDialog({
                content: content
            }),
            art.dialog.list.createHaveError.close()
        }
    }
};