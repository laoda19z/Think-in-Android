package net.onest.zzz.sport.entity;

public class Child {
    private int childId;        //����ID
    private int childAge;       //��������
    private int childGrade;     //�����꼶
    private String childName;   //��������
    private String childSex;    //�����Ա�
    //���췽�����޲Ρ��вΣ�
    public Child() {

    }
    public Child(int childAge, int childGrade, String childName, String childSex) {
        this.childAge = childAge;
        this.childGrade = childGrade;
        this.childName = childName;
        this.childSex = childSex;
    }
    //Getter��Setter����
    public int getChildId() {
        return childId;
    }
    public void setChildId(int childId) {
        this.childId = childId;
    }
    public int getChildAge() {
        return childAge;
    }
    public void setChildAge(int childAge) {
        this.childAge = childAge;
    }
    public int getChildGrade() {
        return childGrade;
    }
    public void setChildGrade(int childGrade) {
        this.childGrade = childGrade;
    }
    public String getChildName() {
        return childName;
    }
    public void setChildName(String childName) {
        this.childName = childName;
    }
    public String getChildSex() {
        return childSex;
    }
    public void setChildSex(String childSex) {
        this.childSex = childSex;
    }

}
