package tripong.backend.entity.role;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RoleResource {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Resource resource;


    ////-----편의 메소드-----
    public RoleResource(Role role){
        this.role=role;
    }

    public void injectResource(Resource resource){
        this.resource = resource;
    }



}
