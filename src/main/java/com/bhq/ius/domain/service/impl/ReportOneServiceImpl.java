package com.bhq.ius.domain.service.impl;

import com.bhq.ius.constant.XmlElement;
import com.bhq.ius.domain.dto.*;
import com.bhq.ius.domain.entity.Course;
import com.bhq.ius.domain.entity.Driver;
import com.bhq.ius.domain.entity.Profile;
import com.bhq.ius.domain.mapper.CourseMapper;
import com.bhq.ius.domain.mapper.DocumentMapper;
import com.bhq.ius.domain.mapper.DriverMapper;
import com.bhq.ius.domain.mapper.ProfileMapper;
import com.bhq.ius.domain.repository.CourseRepository;
import com.bhq.ius.domain.repository.DocumentRepository;
import com.bhq.ius.domain.repository.DriverRepository;
import com.bhq.ius.domain.repository.ProfileRepository;
import com.bhq.ius.domain.service.ReportOneService;
import com.bhq.ius.utils.DataUtil;
import com.bhq.ius.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportOneServiceImpl implements ReportOneService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<DriverDto> uploadFileXml(MultipartFile multipartFile) {
        try {
            DriverXmlDto dto = new DriverXmlDto();
            dto.setDriversDto(new ArrayList<>());
            dto.setDocumentsDto(new ArrayList<>());
            dto.setProfilesDto(new ArrayList<>());
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new ByteArrayInputStream(multipartFile.getBytes()));
            document.getDocumentElement().normalize();
            NodeList nodeListDriver = document.getElementsByTagName(XmlElement.NGUOI_LX.name());
            NodeList nodeListCourse = document.getElementsByTagName(XmlElement.KHOA_HOC.name());


            for (int i = 0; i < nodeListCourse.getLength(); i++) {
                Node node = nodeListCourse.item(i);
                dto.setCourseDto(getCourse(node));

            }

            for (int i = 0; i < nodeListDriver.getLength(); i++) {
                Node node = nodeListDriver.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE && XmlElement.NGUOI_LX.name().equals(node.getNodeName())) {
                    dto.getDriversDto().add(getDriver(node));
                }
                String soCMT = DataUtil.isNullOrEmpty(dto.getDriversDto().get(i).getSoCMT()) ? "NULL" : dto.getDriversDto().get(i).getSoCMT();
                String soCMTCu = DataUtil.isNullOrEmpty(dto.getDriversDto().get(i).getSoCMTCu()) ? "NULL" : dto.getDriversDto().get(i).getSoCMTCu();
                if (node.getNodeType() == Node.ELEMENT_NODE && XmlElement.HO_SO.name().equals(node.getNodeName())) {
                    Element element = (Element) node;
                    NodeList childNodeProfile = XmlUtil.getNodeWithTag(XmlElement.HO_SO.name(), element);
                    dto.getProfilesDto().add(getProfile(childNodeProfile, soCMT, soCMTCu));
                }
                if (node.getNodeType() == Node.ELEMENT_NODE && XmlElement.GIAY_TOS.name().equals(node.getNodeName())) {
                    Element element = (Element) node;
                    NodeList childNodeDocument = XmlUtil.getNodeWithTag(XmlElement.GIAY_TO.name(), element);
                    dto.getDocumentsDto().add(getDocument(childNodeDocument, soCMT, soCMTCu));
                }

//                NodeList childNodeDriver = node.getChildNodes();
//                for (int j = 0; j < childNodeDriver.getLength(); j ++) {
//                    Node childNode = childNodeDriver.item(j);
//                    if (childNode.getNodeType() == Node.ELEMENT_NODE && XmlElement.HO_SO.name().equals(childNode.getNodeName())) {
//                        dto.getProfilesDto().add(getProfile(childNode, soCMT, soCMTCu));
//                    }
//                    if (childNode.getNodeType() == Node.ELEMENT_NODE && XmlElement.GIAY_TOS.name().equals(childNode.getNodeName())) {
//                        dto.getDocumentsDto().add(getDocument(childNode, soCMT, soCMTCu));
//                    }
//                }


            }

            saveIntoDb(dto);

            return dto.getDriversDto();
        } catch (Exception e) {
            log.error("==== error in uploadFileXml ===== {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void saveIntoDb(DriverXmlDto dto) {

        Course course = CourseMapper.INSTANCE.toEntity(dto.getCourseDto());
        List<com.bhq.ius.domain.entity.Document> documents = DocumentMapper.INSTANCE.toEntities(dto.getDocumentsDto());
        List<Driver> drivers = DriverMapper.INSTANCE.toEntities(dto.getDriversDto());
        List<Profile> profiles = ProfileMapper.INSTANCE.toEntities(dto.getProfilesDto());

        courseRepository.save(course);
        drivers = driverRepository.saveAll(drivers);
        profileRepository.saveAll(profiles);
        documentRepository.saveAll(documents);

        dto.setDriversDto(DriverMapper.INSTANCE.toListDto(drivers));

    }

    private DriverDto getDriver(Node node) {
        try {
            DriverDto driverDto = new DriverDto();
            if (node.getNodeType() == Node.ELEMENT_NODE && XmlElement.NGUOI_LX.name().equals(node.getNodeName())) {
                Element element = (Element) node;
                driverDto.setSoTT(XmlUtil.getTagValue("SO_TT", element));
                driverDto.setMaDK(XmlUtil.getTagValue("MA_DK", element));
                driverDto.setHoTenDem(XmlUtil.getTagValue("HO_TEN_DEM", element));
                driverDto.setTen(XmlUtil.getTagValue("TEN", element));
                driverDto.setHoVaTen(XmlUtil.getTagValue("HO_VA_TEN", element));
                driverDto.setNgaySinh(XmlUtil.getTagValue("NGAY_SINH", element));
                driverDto.setTenQuocTich(XmlUtil.getTagValue("TEN_QUOC_TICH", element));
                driverDto.setNoiTT(XmlUtil.getTagValue("NOI_TT", element));
                driverDto.setNoiTTMaDvhc(XmlUtil.getTagValue("NOI_TT_MA_DVHC", element));
                driverDto.setNoiTTMaDvql(XmlUtil.getTagValue("NOI_TT_MA_DVQL", element));
                driverDto.setNoiCT(XmlUtil.getTagValue("NOI_CT", element));
                driverDto.setNoiCTMaDvhc(XmlUtil.getTagValue("NOI_CT_MA_DVHC", element));
                driverDto.setNoiCTMaDvql(XmlUtil.getTagValue("NOI_CT_MA_DVQL", element));
                driverDto.setSoCMT(XmlUtil.getTagValue("SO_CMT", element));
                driverDto.setNgayCapCMT(XmlUtil.getTagValue("NGAY_CAP_CMT", element));
                driverDto.setNoiCapCMT(XmlUtil.getTagValue("NOI_CAP_CMT", element));
                driverDto.setGioiTinh(XmlUtil.getTagValue("GIOI_TINH", element));
                driverDto.setHoVaTenIn(XmlUtil.getTagValue("HO_VA_TEN_IN", element));
                driverDto.setSoCMTCu(XmlUtil.getTagValue("SO_CMND_CU", element));
            }
            return driverDto;
        } catch (Exception e) {
            log.error("==== error in getDriver ==== {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private CourseDto getCourse(Node node) {
        try {
            CourseDto courseDto = new CourseDto();
            if (node.getNodeType() == Node.ELEMENT_NODE && XmlElement.KHOA_HOC.name().equals(node.getNodeName())) {
                Element element = (Element) node;
                courseDto.setMaBCI(XmlUtil.getTagValue("MA_BCI", element));
                courseDto.setMaSoGTVT(XmlUtil.getTagValue("MA_SO_GTVT", element));
                courseDto.setTenSoGTVT(XmlUtil.getTagValue("TEN_SO_GTVT", element));
                courseDto.setMaCSDT(XmlUtil.getTagValue("MA_CSDT", element));
                courseDto.setTenCSDT(XmlUtil.getTagValue("TEN_CSDT", element));
                courseDto.setMaKhoaHoc(XmlUtil.getTagValue("MA_KHOA_HOC", element));
                courseDto.setTenKhoaHoc(XmlUtil.getTagValue("TEN_KHOA_HOC", element));
                courseDto.setMaHangDaoTao(XmlUtil.getTagValue("MA_HANG_DAO_TAO", element));
                courseDto.setHangGPLX(XmlUtil.getTagValue("HANG_GPLX", element));
                courseDto.setSoBCI(XmlUtil.getTagValue("SO_BCI", element));
                courseDto.setNgayBCI(XmlUtil.getTagValue("NGAY_BCI", element));
                courseDto.setLuuLuong(XmlUtil.getTagValue("LUU_LUONG", element));
                courseDto.setSoHocSinh(XmlUtil.getTagValue("SO_HOC_SINH", element));
                courseDto.setNgayKhaiGiang(XmlUtil.getTagValue("NGAY_KHAI_GIANG", element));
                courseDto.setNgayBeGiang(XmlUtil.getTagValue("NGAY_BE_GIANG", element));
                courseDto.setSoQDKG(XmlUtil.getTagValue("SO_QD_KG", element));
                courseDto.setNgayQDKG(XmlUtil.getTagValue("NGAY_QD_KG", element));
                courseDto.setNgaySatHach(XmlUtil.getTagValue("NGAY_SAT_HACH", element));
                courseDto.setThoiGianDT(XmlUtil.getTagValue("THOI_GIAN_DT", element));
            }
            return courseDto;
        } catch (Exception e) {
            log.error("==== error in getCourse ==== {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private ProfileDto getProfile(NodeList nodeList, String soCMT, String soCMTCu) {
        try {
            ProfileDto dto = new ProfileDto();
            for (int j = 0; j < nodeList.getLength(); j ++) {
                Node node = nodeList.item(j);
                if (node.getNodeType() == Node.ELEMENT_NODE && XmlElement.HO_SO.name().equals(node.getNodeName())) {
                    Element element = (Element) node;
                    dto.setSoHoSo(XmlUtil.getTagValue("SO_HO_SO", element));
                    dto.setMaDVNhanHoSo(XmlUtil.getTagValue("MA_DV_NHAN_HOSO", element));
                    dto.setTenDVNhanHoSo(XmlUtil.getTagValue("TEN_DV_NHAN_HOSO", element));
                    dto.setNgayNhanHoSo(XmlUtil.getTagValue("NGAY_NHAN_HOSO", element));
                    dto.setNguoiNhanHoSo(XmlUtil.getTagValue("NGUOI_NHAN_HOSO", element));
                    dto.setMaLoaiHoSo(XmlUtil.getTagValue("MA_LOAI_HOSO", element));
                    dto.setTenLoaiHoSo(XmlUtil.getTagValue("TEN_LOAI_HOSO", element));
                    dto.setAnhChanDung(XmlUtil.getTagValue("ANH_CHAN_DUNG", element));
                    dto.setChatLuongAnh(XmlUtil.getTagValue("CHAT_LUONG_ANH", element));
                    dto.setNgayThuNhanAnh(XmlUtil.getTagValue("NGAY_THU_NHAN_ANH", element));
                    dto.setNguoiThuNhanAnh(XmlUtil.getTagValue("NGUOI_THU_NHAN_ANH", element));
                    dto.setSoGPLXDaCo(XmlUtil.getTagValue("SO_GPLX_DA_CO", element));
                    dto.setHangGPLXDaCo(XmlUtil.getTagValue("HANG_GPLX_DA_CO", element));
                    dto.setDvCapGPLXDaCo(XmlUtil.getTagValue("DV_CAP_GPLX_DACO", element));
                    dto.setTenDVCapGPLXDaCo(XmlUtil.getTagValue("TEN_DV_CAP_GPLX_DACO", element));
                    dto.setNoiCapGPLXDaCo(XmlUtil.getTagValue("NOI_CAP_GPLX_DACO", element));
                    dto.setNgayCapGPLXDaCo(XmlUtil.getTagValue("NGAY_CAP_GPLX_DACO", element));
                    dto.setNgayHHGPLXDaCo(XmlUtil.getTagValue("NGAY_HH_GPLX_DACO", element));
                    dto.setNgayTTGPLXDaCo(XmlUtil.getTagValue("NGAY_TT_GPLX_DACO", element));
                    dto.setMaNoiHocLaiXe(XmlUtil.getTagValue("MA_NOI_HOC_LAIXE", element));
                    dto.setTenNoiHocLaiXe(XmlUtil.getTagValue("TEN_NOI_HOC_LAIXE", element));
                    dto.setNamHocLaiXe(XmlUtil.getTagValue("NAM_HOC_LAIXE", element));
                    dto.setSoNamLaiXe(XmlUtil.getTagValue("SO_NAM_LAIXE", element));
                    dto.setSoKMAnToan(XmlUtil.getTagValue("SO_KM_ANTOAN", element));
                    dto.setGiayCNSK(XmlUtil.getTagValue("GIAY_CNSK", element));
                    dto.setHinhThucCap(XmlUtil.getTagValue("HINH_THUC_CAP", element));
                    dto.setHangGPLX(XmlUtil.getTagValue("HANG_GPLX", element));
                    dto.setHangDaoTao(XmlUtil.getTagValue("HANG_DAOTAO", element));
                    dto.setChonInGPLX(XmlUtil.getTagValue("CHON_IN_GPLX", element));
                    if (DataUtil.isNullOrEmpty(soCMT)) {
                        dto.setSoCMT(soCMT);
                    }
                    if (DataUtil.isNullOrEmpty(soCMTCu)) {
                        dto.setSoCMTCu(soCMTCu);
                    }
                }
            }
            return dto;
        } catch (Exception e) {
            log.error("==== error in getCourse ==== {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private DocumentDto getDocument(NodeList nodeList, String soCMT, String soCMTCu) {
        try {
            DocumentDto dto = new DocumentDto();
            for (int j = 0; j < nodeList.getLength(); j ++) {
                Node childNode = nodeList.item(j);
                if (childNode.getNodeType() == Node.ELEMENT_NODE && XmlElement.GIAY_TO.name().equals(childNode.getNodeName())) {
                    Element element = (Element) childNode;
                    dto.setMaGiayTo(XmlUtil.getTagValue("MA_GIAY_TO", element));
                    dto.setTenGiayTo(XmlUtil.getTagValue("TEN_GIAY_TO", element));
                    if(DataUtil.isNullOrEmpty(soCMT)) {
                        dto.setSoCMT(soCMT);
                    }
                    if(DataUtil.isNullOrEmpty(soCMTCu)) {
                        dto.setSoCMTCu(soCMTCu);
                    }
                }
            }
            return dto;
        } catch (Exception e) {
            log.error("==== error in getCourse ==== {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
