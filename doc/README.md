HGIndicator.Trigger("srcTarget", "event_Name")
	.StatusCheck(
	.Rule("dstTarget", "event_name")
	.commit();


srcTarget에서 StatusCheck 부분(B)이 true가 되면
- srcTarget
(1) CheckBox : all check
(2) EditText : all written or necessary parts
(3) ScrollView : Top or down

() Status라는 부분이 기본적으로 3가지 상태가 존재하고, event_name이 이거라면 status 체크는 그렇게 동작하도록
//enum Type을 사용하고 싶기는 한데, 명확히 어떻게 사용할지 잘 모르겠다.

* setCustomStatus : Status를 체크하는 함수들의 일부 인자들을 수정하도

dstTarget에서 Actions Event가 가해짐.

Trigger에서 해줘야 하는 부분은 
- 어는 부분을 볼 것인지를 알려주고 
- 그리고 어떤 이벤트에 따라서 어떤 statusChecker가 동작할 것인지에 대하여 알려줘야함.
- 그러면 매개변수로 (View, event_name) 만 정해주면 알아서 해줘야하는 것이 맞음.

그런데 이제 그런 문제가 생김 이거까지는 알려줬는데 그럼 이제 동작을 해야하는 데, 이거랑 Action 부분을 연결시켜줘야함.

같은 List Index로써 연결한다면 문제는 없으려나? 

어떤 버튼에 HGI를 다는 형식이야.
HGIndicator.Trigger("srcTarget", "trigger_Name"), trigger_name : {"Except", "all check", "scroll_bottom", "scroll_up", "empty_text"}
	.Rule("dstTarget", "event_name"), event_name : {"HIGHLIGHT", "FOCUS", "POINTER"}
	.commit();
	.AddRule("Trigger_name", "dst", "event_name");

그런데 이거, 굳이 List로 저장할 필요가 있나?
그렇게 되면, 실행할 때마다 계속해서 add되는 현상이 나타날 꺼임.

동작형태
1. 미리 다 등록시킨 다음에 특정 trigger 이름을 호출 해서 commit(trig_name, event_name) 하는 형태
2. 그 때 그때 등록시켜서 commit() 하는 형태
