package com.vrsabu.diagnosisai.DTO;

import lombok.Data;

@Data
public class GetAllUserResponse {

    private String id;
    private String name;
    private String gender;
    private int age;
    private String email;
}
