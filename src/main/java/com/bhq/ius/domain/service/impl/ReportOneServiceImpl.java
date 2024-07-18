package com.bhq.ius.domain.service.impl;

import com.bhq.ius.constant.IusConstant;
import com.bhq.ius.constant.RecordState;
import com.bhq.ius.constant.XmlElement;
import com.bhq.ius.domain.dto.*;
import com.bhq.ius.domain.dto.common.BaseResponseData;
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
import com.bhq.ius.integration.service.IntegrationUserSerive;
import com.bhq.ius.utils.DataUtil;
import com.bhq.ius.utils.XmlUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private IntegrationUserSerive integrationUserSerive;

    @Override
    @Transactional
    public CourseDto uploadFileXml(MultipartFile file) {
        try {
            /* remove old record */
            removeOldRecord();

            DriverXmlDto dto = new DriverXmlDto();
            dto.setDriversDto(new ArrayList<>());
            dto.setDocumentsDto(new ArrayList<>());
            dto.setProfilesDto(new ArrayList<>());
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new ByteArrayInputStream(file.getBytes()));
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
                    DriverDto driverDto = getDriver(node);
                    dto.getDriversDto().add(driverDto);
                    String soCMT = DataUtil.isNullOrEmpty(dto.getDriversDto().get(i).getSoCMT()) ? "NULL" : dto.getDriversDto().get(i).getSoCMT();
                    String soCMTCu = DataUtil.isNullOrEmpty(dto.getDriversDto().get(i).getSoCMTCu()) ? "NULL" : dto.getDriversDto().get(i).getSoCMTCu();
                    String uuid = driverDto.getUuid();
                    Element element = (Element) node;
                    NodeList childNodeProfile = XmlUtil.getNodeWithTag(XmlElement.HO_SO.name(), element);
                    dto.getProfilesDto().add(getProfile(childNodeProfile, soCMT, soCMTCu, uuid));
                    NodeList childNodeDocument = XmlUtil.getNodeWithTag(XmlElement.GIAY_TO.name(), element);
                    dto.getDocumentsDto().addAll(getDocument(childNodeDocument, soCMT, soCMTCu, uuid));
                }

            }

            /* save into db */
            saveIntoDb(dto);

            return dto.getCourseDto();
        } catch (Exception e) {
            log.error("==== error in uploadFileXml ===== {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public BaseResponseData<List<Long>> submitDriver(List<Long> listId, Long courseId) {
        BaseResponseData<List<Long>> responseData = new BaseResponseData<>();
        try {
            List<Driver> drivers = getListDriver(listId, courseId);
            List<Long> listIdSubmitted = integrationUserSerive.CreateDrivers(drivers);
            responseData.initData(listIdSubmitted);
        } catch (Exception exception) {
            log.error("==== error in submitDriver ==== {}", exception.getMessage());
            responseData.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseData.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseData;
    }

    @Override
    public BaseResponseData<List<Long>> submitCourse(List<Long> listId) {
        BaseResponseData<List<Long>> responseData = new BaseResponseData<>();
        try {
            List<Course> courses = getListCourse(listId);
//            List<Course> courses = courseRepository.findAllById(listId);
            List<Long> listIdSubmitted = integrationUserSerive.CreateCourses(courses);
            responseData.initData(listIdSubmitted);
        } catch (Exception exception) {
            log.error("==== error in submitDriver ==== {}", exception.getMessage());
            responseData.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseData.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseData;
    }

    @Override
    public BaseResponseData<List<Long>> submitAvatar(List<Long> listId, Long courseId) {
        BaseResponseData<List<Long>> responseData = new BaseResponseData<>();
        try {
            List<Driver> drivers = getListDriverForAvatar(listId, courseId);
            List<Long> listIdSubmitted = integrationUserSerive.UpdateUserPicture(drivers);
            responseData.initData(listIdSubmitted);
        } catch (Exception exception) {
            log.error("==== error in submitDriver ==== {}", exception.getMessage());
            responseData.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseData.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseData;
    }

    @Override
    public BaseResponseData<List<Long>> submitEnroll(List<Long> listId, Long courseId) {
        BaseResponseData<List<Long>> responseData = new BaseResponseData<>();
        try {
            List<Driver> drivers = getListDriverForEnroll(listId, courseId);
            List<Long> listIdSubmitted = integrationUserSerive.UpdateUserEnroll(drivers);
            responseData.initData(listIdSubmitted);
        } catch (Exception exception) {
            log.error("==== error in submitDriver ==== {}", exception.getMessage());
            responseData.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseData.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseData;
    }

    @Override
    public BaseResponseData<ReportOneInfoDto> getReportOneInfo(Optional<Long> id) {
        BaseResponseData<ReportOneInfoDto> responseData = new BaseResponseData<>();
        ReportOneInfoDto reportOneInfoDto = new ReportOneInfoDto();
//        Optional<Course> course = courseRepository.findById(id);
//        if (course.isPresent()) {
        Long driverSuccess = driverRepository.countByStateIn(Collections.singletonList(RecordState.SUBMITTED));
        Long driverFailed = driverRepository.countByStateIn(Collections.singletonList(RecordState.FAILED));
        Long driverNotSubmitted = driverRepository.count() - driverSuccess - driverFailed;

        Long profileSuccess = profileRepository.countByStateIn(Collections.singletonList(RecordState.SUBMITTED));
        Long profileFailed = profileRepository.countByStateIn(Collections.singletonList(RecordState.FAILED));
        Long profileNotSubmitted = profileRepository.count() - profileSuccess - profileFailed;

        Long enrollSuccess = driverRepository.countByStateEnrollIn(Collections.singletonList(RecordState.SUBMITTED));
        Long enrollFailed = driverRepository.countByStateEnrollIn(Collections.singletonList(RecordState.FAILED));
        Long enrollNotSubmitted = driverRepository.count() - enrollSuccess - enrollFailed;

        reportOneInfoDto.setDriverSuccess(driverSuccess);
        reportOneInfoDto.setDriverFailed(driverFailed);
        reportOneInfoDto.setDriverNotSubmitted(driverNotSubmitted);

        reportOneInfoDto.setProfileSuccess(profileSuccess);
        reportOneInfoDto.setProfileFailed(profileFailed);
        reportOneInfoDto.setProfileNotSubmitted(profileNotSubmitted);

        reportOneInfoDto.setEnrollSuccess(enrollSuccess);
        reportOneInfoDto.setEnrollFailed(enrollFailed);
        reportOneInfoDto.setEnrollNotSubmitted(enrollNotSubmitted);

        responseData.setData(reportOneInfoDto);
//        }

        return responseData;
    }

    @Override
    public BaseResponseData<List<Driver>> testGetDriver(List<Long> listId, Long courseId) {
        BaseResponseData<List<Driver>> responseData = new BaseResponseData<>();
        try {
            List<Driver> drivers = getListDriver(listId, courseId);
            responseData.initData(drivers);
            return responseData;
        } catch (Exception exception) {
            log.error("==== error in testGetDriver ==== {}", exception.getMessage());
            responseData.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseData.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return responseData;
    }

    @Override
    public BaseResponseData<?> testPostImage() {
        BaseResponseData<?> responseData = new BaseResponseData<>();
        integrationUserSerive.testPostImage();
        return responseData;
    }


    @Transactional
    public void removeOldRecord() {
        try {
            log.info("===== removeOldRecord =====");
            documentRepository.deleteAll();
            profileRepository.deleteAll();
            driverRepository.deleteAll();
            courseRepository.deleteAll();
            log.info("===== removeOldRecord success =====");
        } catch (Exception exception) {
            log.error("===== error removeOldRecord ==== {}", exception.getMessage());
        }
    }

    private void saveIntoDb(DriverXmlDto dto) {

        Course course = CourseMapper.INSTANCE.toEntity(dto.getCourseDto());
        List<com.bhq.ius.domain.entity.Document> documents = DocumentMapper.INSTANCE.toEntities(dto.getDocumentsDto());
        List<Driver> drivers = DriverMapper.INSTANCE.toEntities(dto.getDriversDto());
        List<Profile> profiles = ProfileMapper.INSTANCE.toEntities(dto.getProfilesDto());
        for (Driver driver : drivers) {
            driver.setCourse(course);
            List<com.bhq.ius.domain.entity.Document> doc = documents.stream().filter(x -> x.getSoCMT().equals(driver.getSoCMT())).map(x -> {
                x.setDriver(driver);
                return x;
            }).toList();
            Optional<Profile> profile = profiles.stream().filter(x -> x.getSoCMT().equals(driver.getSoCMT())).map(x -> {
                byte[] byteImageArray = DataUtil.base64Jp2toImageJpg(x.getAnhChanDung());
                if (!DataUtil.isNullOrEmpty(byteImageArray)) {
                    x.setImageFile(byteImageArray);
                }
                x.setDriver(driver);
                return x;
            }).findFirst();
            driverRepository.save(driver);
        }

        Course courseEntity = courseRepository.save(course);
        dto.setCourseDto(CourseMapper.INSTANCE.toDto(courseEntity));
        dto.setDriversDto(DriverMapper.INSTANCE.toListDto(drivers));
        profileRepository.saveAll(profiles);
        documentRepository.saveAll(documents);
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


                if (!DataUtil.isNullOrEmpty(XmlUtil.getTagValue("NGAY_SINH", element))) {
                    String ngaySinh = DataUtil.convertDateOfBirthWithFormat(XmlUtil.getTagValue("NGAY_SINH", element));
                    driverDto.setNgaySinh(DataUtil.convertStringToLocalDate(ngaySinh, IusConstant.DATE_FORMAT));
                }

                driverDto.setTenQuocTich(XmlUtil.getTagValue("TEN_QUOC_TICH", element));
                driverDto.setNoiTT(XmlUtil.getTagValue("NOI_TT", element));
                driverDto.setNoiTTMaDvhc(XmlUtil.getTagValue("NOI_TT_MA_DVHC", element));
                driverDto.setNoiTTMaDvql(XmlUtil.getTagValue("NOI_TT_MA_DVQL", element));
                driverDto.setNoiCT(XmlUtil.getTagValue("NOI_CT", element));
                driverDto.setNoiCTMaDvhc(XmlUtil.getTagValue("NOI_CT_MA_DVHC", element));
                driverDto.setNoiCTMaDvql(XmlUtil.getTagValue("NOI_CT_MA_DVQL", element));
                driverDto.setSoCMT(XmlUtil.getTagValue("SO_CMT", element));

                String ngayCapCMT = XmlUtil.getTagValue("NGAY_CAP_CMT", element);
                if (!DataUtil.isNullOrEmpty(ngayCapCMT)) {
                    driverDto.setNgayCapCMT(DataUtil.convertStringToLocalDate(ngayCapCMT, IusConstant.DATE_FORMAT));
                }

                driverDto.setNoiCapCMT(XmlUtil.getTagValue("NOI_CAP_CMT", element));
                driverDto.setGioiTinh(XmlUtil.getTagValue("GIOI_TINH", element));
                driverDto.setHoVaTenIn(XmlUtil.getTagValue("HO_VA_TEN_IN", element));
                driverDto.setSoCMTCu(XmlUtil.getTagValue("SO_CMND_CU", element));
                driverDto.setUuid(UUID.randomUUID().toString());
            }
            return driverDto;
        } catch (Exception e) {
            log.error("==== error in getDriver ==== {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    protected List<Driver> getListDriver(List<Long> listId, Long courseId) {
        List<Driver> drivers = new ArrayList<>();
        if (DataUtil.isNullOrEmpty(listId)) {
            drivers = driverRepository.findAllByStateNullOrStateNotIn(courseId);
        } else {
            drivers = driverRepository.findAllById(listId);
        }
        return drivers;
    }

    protected List<Driver> getListDriverForEnroll(List<Long> listId, Long courseId) {
        List<Driver> drivers = new ArrayList<>();
        Optional<Course> course = courseRepository.findById(courseId);
        if (DataUtil.isNullOrEmpty(listId)) {
            drivers = driverRepository.findAllByStateEnrollNullOrStateEnrollNotInAndCourse(Collections.singletonList(RecordState.SUBMITTED), course.get());
        } else {
            drivers = driverRepository.findAllById(listId);
        }
        return drivers;
    }

    protected List<Driver> getListDriverForAvatar(List<Long> listId, Long courseId) {
        List<Driver> drivers = new ArrayList<>();
        if (DataUtil.isNullOrEmpty(listId)) {
            drivers = driverRepository.findAllByStateNullOrStateNotInWithProfile(courseId);
        } else {
            drivers = driverRepository.findAllById(listId);
        }
        return drivers;
    }

    protected List<Course> getListCourse(List<Long> listId) {
        List<Course> items = new ArrayList<>();
        if (DataUtil.isNullOrEmpty(listId)) {
            items = courseRepository.findAllByStateNullOrStateNotIn(Collections.singletonList(RecordState.SUBMITTED));
        } else {
            items = courseRepository.findAllById(listId);
        }
        return items;
    }

    protected List<Profile> getListProfile(List<Long> listId) {
        List<Profile> items = new ArrayList<>();
        if (DataUtil.isNullOrEmpty(listId)) {
            items = profileRepository.findAllByStateNullOrStateNotIn(Collections.singletonList(RecordState.SUBMITTED));
        } else {
            items = profileRepository.findAllById(listId);
        }
        return items;
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

                String ngayBCi = XmlUtil.getTagValue("NGAY_BCI", element);
                if (!DataUtil.isNullOrEmpty(ngayBCi)) {
                    courseDto.setNgayBCI(DataUtil.convertStringToLocalDate(ngayBCi, IusConstant.DATE_FORMAT));
                }

                courseDto.setLuuLuong(XmlUtil.getTagValue("LUU_LUONG", element));
                courseDto.setSoHocSinh(XmlUtil.getTagValue("SO_HOC_SINH", element));

                String ngayKhaiGiang = XmlUtil.getTagValue("NGAY_KHAI_GIANG", element);
                if (!DataUtil.isNullOrEmpty(ngayKhaiGiang)) {
                    courseDto.setNgayKhaiGiang(DataUtil.convertStringToLocalDate(ngayKhaiGiang, IusConstant.DATE_FORMAT));
                }

                String ngayBeGiang = XmlUtil.getTagValue("NGAY_BE_GIANG", element);
                if (!DataUtil.isNullOrEmpty(ngayBeGiang)) {
                    courseDto.setNgayBeGiang(DataUtil.convertStringToLocalDate(ngayBeGiang, IusConstant.DATE_FORMAT));
                }

                courseDto.setSoQDKG(XmlUtil.getTagValue("SO_QD_KG", element));

                String ngayQDKG = XmlUtil.getTagValue("NGAY_QD_KG", element);
                if (!DataUtil.isNullOrEmpty(ngayQDKG)) {
                    courseDto.setNgayQDKG(DataUtil.convertStringToLocalDate(ngayQDKG, IusConstant.DATE_FORMAT));
                }

                String ngaySatHach = XmlUtil.getTagValue("NGAY_SAT_HACH", element);
                if (!DataUtil.isNullOrEmpty(ngaySatHach)) {
                    courseDto.setNgaySatHach(DataUtil.convertStringToLocalDate(ngaySatHach, IusConstant.DATE_FORMAT));
                }

                courseDto.setThoiGianDT(XmlUtil.getTagValue("THOI_GIAN_DT", element));
                courseDto.setUuid(UUID.randomUUID().toString());
            }
            return courseDto;
        } catch (Exception e) {
            log.error("==== error in getCourse ==== {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private ProfileDto getProfile(NodeList nodeList, String soCMT, String soCMTCu, String uuid) {
        try {
            ProfileDto dto = new ProfileDto();
            for (int j = 0; j < nodeList.getLength(); j++) {
                Node node = nodeList.item(j);
                if (node.getNodeType() == Node.ELEMENT_NODE && XmlElement.HO_SO.name().equals(node.getNodeName())) {
                    Element element = (Element) node;
                    dto.setSoHoSo(XmlUtil.getTagValue("SO_HO_SO", element));
                    dto.setMaDVNhanHoSo(XmlUtil.getTagValue("MA_DV_NHAN_HOSO", element));
                    dto.setTenDVNhanHoSo(XmlUtil.getTagValue("TEN_DV_NHAN_HOSO", element));
                    String ngayNhanHoSo = XmlUtil.getTagValue("NGAY_NHAN_HOSO", element);

                    if (!DataUtil.isNullOrEmpty(ngayNhanHoSo)) {
                        dto.setNgayNhanHoSo(ngayNhanHoSo);
                    }

                    dto.setNguoiNhanHoSo(XmlUtil.getTagValue("NGUOI_NHAN_HOSO", element));
                    dto.setMaLoaiHoSo(XmlUtil.getTagValue("MA_LOAI_HOSO", element));
                    dto.setTenLoaiHoSo(XmlUtil.getTagValue("TEN_LOAI_HOSO", element));
                    dto.setAnhChanDung(XmlUtil.getTagValue("ANH_CHAN_DUNG", element));
                    dto.setChatLuongAnh(XmlUtil.getTagValue("CHAT_LUONG_ANH", element));

                    String ngayThuNhanAnh = XmlUtil.getTagValue("NGAY_THU_NHAN_ANH", element);
                    if (!DataUtil.isNullOrEmpty(ngayThuNhanAnh)) {
                        dto.setNgayThuNhanAnh(ngayThuNhanAnh);
                    }

                    dto.setNguoiThuNhanAnh(XmlUtil.getTagValue("NGUOI_THU_NHAN_ANH", element));
                    dto.setSoGPLXDaCo(XmlUtil.getTagValue("SO_GPLX_DA_CO", element));
                    dto.setHangGPLXDaCo(XmlUtil.getTagValue("HANG_GPLX_DA_CO", element));
                    dto.setDvCapGPLXDaCo(XmlUtil.getTagValue("DV_CAP_GPLX_DACO", element));
                    dto.setTenDVCapGPLXDaCo(XmlUtil.getTagValue("TEN_DV_CAP_GPLX_DACO", element));
                    dto.setNoiCapGPLXDaCo(XmlUtil.getTagValue("NOI_CAP_GPLX_DACO", element));

                    String ngayCapGPLXDaCo = XmlUtil.getTagValue("NGAY_CAP_GPLX_DACO", element);
                    if (!DataUtil.isNullOrEmpty(ngayCapGPLXDaCo)) {
                        dto.setNgayCapGPLXDaCo(ngayCapGPLXDaCo);
                    }
                    String ngayHHGPLXDaCo = XmlUtil.getTagValue("NGAY_HH_GPLX_DACO", element);
                    if (!DataUtil.isNullOrEmpty(ngayHHGPLXDaCo)) {
                        dto.setNgayHHGPLXDaCo(ngayHHGPLXDaCo);
                    }
                    String ngayTTGPLXDaCo = XmlUtil.getTagValue("NGAY_TT_GPLX_DACO", element);
                    if (!DataUtil.isNullOrEmpty(ngayTTGPLXDaCo)) {
                        dto.setNgayTTGPLXDaCo(ngayTTGPLXDaCo);
                    }
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
                    if (!DataUtil.isNullOrEmpty(soCMT)) {
                        dto.setSoCMT(soCMT);
                    }
                    if (!DataUtil.isNullOrEmpty(soCMTCu)) {
                        dto.setSoCMTCu(soCMTCu);
                    }
                    dto.setDriver_uuid(uuid);
                }
            }
            return dto;
        } catch (Exception e) {
            log.error("==== error in getCourse ==== {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<DocumentDto> getDocument(NodeList nodeList, String soCMT, String soCMTCu, String uuid) {
        try {
            List<DocumentDto> listDto = new ArrayList<>();
            for (int j = 0; j < nodeList.getLength(); j++) {
                Node childNode = nodeList.item(j);
                DocumentDto dto = new DocumentDto();
                if (childNode.getNodeType() == Node.ELEMENT_NODE && XmlElement.GIAY_TO.name().equals(childNode.getNodeName())) {
                    Element element = (Element) childNode;
                    dto.setMaGiayTo(XmlUtil.getTagValue("MA_GIAY_TO", element));
                    dto.setTenGiayTo(XmlUtil.getTagValue("TEN_GIAY_TO", element));
                    if (!DataUtil.isNullOrEmpty(soCMT)) {
                        dto.setSoCMT(soCMT);
                    }
                    if (!DataUtil.isNullOrEmpty(soCMTCu)) {
                        dto.setSoCMTCu(soCMTCu);
                    }
                    dto.setDriver_uuid(uuid);
                    listDto.add(dto);
                }
            }
            return listDto;
        } catch (Exception e) {
            log.error("==== error in getCourse ==== {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
