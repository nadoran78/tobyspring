#### getClass().getResource()  vs getClass.getClassLoader.getResource()
- getClass().getResource()를 사용할 때는 "/numbers.txt"와 같이 찾으려는 리소스 이름 앞에 /를 붙여줘야 한다. 그렇지 않으면 NPE 발생
- getClass().getClassLoader().getResource()를 사용할 때는 "numbers.txt"를 사용해도 된다.

### 동일성(identity)과 동등성(equality)
- 동일성 : 두 개의 객체가 완전히 같은 경우
- 동등성 : 두 개의 객체가 같은 정보를 갖고 있는 경우