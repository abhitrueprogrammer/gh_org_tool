package com.uni.ghorgtool.dto.response;

import java.util.List;

import com.uni.ghorgtool.models.Org;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MeResponse {
    private String email;
    private List<Org> org;
}
