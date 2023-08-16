import WeddingDday from "../components/Home/WeddingDday";
import WeddingFair from "../components/Home/WeddingFair"
import PlanTips from "../components/Home/PlanTips";
import { useSetRecoilState, useResetRecoilState } from "recoil";
import { userState } from "../recoil/ProfileAtom";
import { customAxios } from "../lib/axios";
import { useEffect, useState } from "react";
import classes from "./Home.module.css";
import { weddingDateState } from "../recoil/WeddingDdayAtom";
import TipMagazine from "../components/Home/TipMagazine";
import Ranking from "../components/Home/Ranking"
import LatestSeenProduct from "../components/Home/LatestSeenProduct"

function Home() {

  const setUser = useSetRecoilState(userState);
  const resetUserState = useResetRecoilState(userState);
  const setWeddingDate = useSetRecoilState(weddingDateState);

  const [productType, setProductType] = useState('HALL');

  const handleProductTypeClick = (type) => {
    setProductType(type);
  };

  const setThemeState = (gender) => {
    const rootElement = document.documentElement;

    switch (gender) {
      case "MALE":
        rootElement.style.setProperty("--color-point", "var(--color-groom)");
        break;
      case "FEMALE":
        rootElement.style.setProperty("--color-point", "var(--color-brider)");
        break;
    }
  }

  const updateUser = async () => {
    try {
      // 유저 정보 조회
      const res = await customAxios.get("profile");
      if (res.data) {
        setUser(res.data.result[0]);
        fetchWeddingDate();
        if (res.data.result[0]?.gender) {
          setThemeState(res.data.result[0].gender)
        }
      }
    } catch (error) {
      // 유저 정보 초기화
      resetUserState();
      console.error("유저 정보 API 요청 에러", error);
      // navigate("/");
    }
  };

  const fetchWeddingDate = async () => {
    try {
      const response = await customAxios.get("wedding-date");
      // 날짜(YYYY-MM-DD) 형태로만 받기
      setWeddingDate(response.data.result[0].weddingDate);
    } catch (error) {
      // console.log("결혼식 날짜 없음");
    }
  };

  useEffect(() => {
    updateUser();
  }, []);

  return (
    <div className={classes.container}>
        <WeddingDday />
      <div className={classes.top}>
        <PlanTips />
        {/* <p className={classes.word}> Wedding Tips</p>
        <hr className={classes.hr} />
        <Tipbox /> */}
      </div>
      <div>
        <WeddingFair />
      </div>
      <div>
        <TipMagazine />
      </div>
      <div>
        <div>
          <div onClick={() => handleProductTypeClick('HALL')}>예식장</div>
          <div onClick={() => handleProductTypeClick('STUDIO')}>스튜디오</div>
          <div onClick={() => handleProductTypeClick('DRESS')}>드레스</div>
          <div onClick={() => handleProductTypeClick('MAKEUP')}>메이크업</div>
        </div>
        <Ranking productType={productType} />
      </div>
      <div>
        <LatestSeenProduct />
      </div>
    </div>
  );
}

export default Home;
