package com.vrsabu.diagnosisai.web;

import com.vrsabu.diagnosisai.config.MlApiProperties;
import com.vrsabu.diagnosisai.service.AnalysisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AnalysisController.class)
class AnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalysisService analysisService;

    @MockBean
    private MlApiProperties mlApiProperties; // to satisfy @EnableConfigurationProperties context

    @Test
    void diagnose_returnsString() throws Exception {
        when(analysisService.analyze(any(), any(), any(), any(), any())).thenReturn("OK_RESULT");

        MockMultipartFile cc = new MockMultipartFile("chief_complaint", "cc.txt", "text/plain", "A".getBytes());
        MockMultipartFile hpi = new MockMultipartFile("history_of_present_illness", "hpi.txt", "text/plain", "B".getBytes());
        MockMultipartFile ref = new MockMultipartFile("relevant_exam_findings", "ref.txt", "text/plain", "C".getBytes());
        MockMultipartFile lab = new MockMultipartFile("lab_investigation", "lab.txt", "text/plain", "D".getBytes());
        MockMultipartFile ot = new MockMultipartFile("ongoing_treatments", "ot.txt", "text/plain", "E".getBytes());

        mockMvc.perform(multipart("/api/analysis/diagnose")
                        .file(cc)
                        .file(hpi)
                        .file(ref)
                        .file(lab)
                        .file(ot)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("OK_RESULT"));
    }
}
