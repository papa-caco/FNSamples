package ar.com.lpa.ldapExchanger.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter (AccessLevel.PUBLIC)
@Entity(name = "SecurableObject")
@Table(name = "SecurableObject")
@NoArgsConstructor
public class SecurableObject {
    @Id
    @GeneratedValue
    @Column(name = "idSecurableObject")
    private int idSecurableObject;

    @Column(name = "tableName")
    @NonNull
    private String tableName;

    @Column(name = "lineCount")
    @NonNull
    private int lineCount;

    @Column(name = "fnObjectType")
    @Enumerated(EnumType.STRING)git
    private FnObjectType fnObjectType;

    @Column(name = "objectCount")
    private int objectCount;

    @Column(name = "securityIdCount")
    @NonNull
    private int securityIdCount;

    @Column(name = "processStatus")
    private char processStatus;

    public SecurableObject(String tableName, int lineCount, int securityIdCount){
        this.tableName = tableName;
        this.lineCount = lineCount;
        this.securityIdCount = securityIdCount;
        this.processStatus = 'N';
    }
}
