# JAVA_BANK 侘寂風金融網站 UI/UX 優化建議

## 專案定位

本專題目前已具備：

- 東方侘寂美學
- 高級留白感
- 穩定金融品牌氣質
- Calm / Balance / Trust 的品牌一致性

整體方向正確，已超出一般學生作品常見的「日系模板感」。

目前最需要加強的是：

1. Typography（字體層級）
2. Spacing Rhythm（留白節奏）
3. Texture（材質感）
4. Motion（微動畫）
5. 金融秩序感

---

# 一、色彩系統（不可新增主色）

## 已確立的 CSS 色票

```css
:root {
  /* 主色（背景） */
  --bg-primary: #F5F1EA;

  /* 次背景（卡片） */
  --bg-secondary: #EAE4DA;

  /* 主文字 */
  --text-primary: #2B2B2B;

  /* 次文字 */
  --text-secondary: #6E6259;

  /* 主品牌色（穩重銀行） */
  --primary: #5C6B5F;

  /* hover / 強調 */
  --primary-dark: #3F4A42;

  /* 線條 / 邊框 */
  --border: #D6CEC3;

  /* 點綴（印章紅） */
  --accent: #A65A4D;
}
```

---

## 色彩使用原則

### 背景

- 主背景維持 `--bg-primary`
- 不要使用純白 `#FFFFFF`
- 保持紙感、和紙感、暖灰感

---

### 卡片

目前卡片太白，容易產生：

- SaaS dashboard 感
- Bootstrap 感
- 科技產品頁感

建議：

```css
background: var(--bg-secondary);
border: 1px solid var(--border);
box-shadow: none;
```

避免：

```css
background: #ffffff;
box-shadow: 0 10px 30px rgba(0,0,0,0.1);
```

侘寂風不適合強陰影。

---

### Accent 使用規則

`--accent` 僅能作為：

- 小印章
- 極小面積重點
- icon 點綴
- hover 微提示

不可大面積使用。

避免：

- 大紅色按鈕
- 紅色區塊背景
- 高彩度視覺焦點

---

# 二、Typography（最需要改善）

## 目前問題

現在文字層級過於平均：

- 太工整
- 太安全
- 缺少呼吸感
- 有預設字體感

導致畫面雖然好看，但仍有學生作品氣息。

---

# Hero 標題建議

目前：

```txt
靜心理財
安穩致遠
```

建議：

- 第二行字重更重
- 行距更近
- 字距略縮
- 建立視覺重心

---

## 建議字體

### 中文

優先：

- 思源宋體 Heavy
- Noto Serif TC

避免：

- 過度現代黑體
- 圓體
- 過度文青手寫字

---

### 英文

推薦：

- Cormorant Garamond
- EB Garamond
- Libre Baskerville

避免：

- Montserrat
- Poppins
- 過度 SaaS 感字體

---

# 三、Spacing Rhythm（留白節奏）

## 目前問題

所有區塊間距過於平均。

畫面有：

- Bootstrap 排版感
- 規律過頭
- 缺少情緒停頓

---

# 建議

## Hero 區與 Services 區

目前間距：

太剛好。

建議：

增加額外空白。

例如：

```css
margin-bottom: 140px;
```

目的：

創造：

- 停頓感
- 呼吸感
- 翻頁感
- 禪意節奏

---

# 間距原則

侘寂風不要：

```txt
80 / 80 / 80 / 80
```

而是：

```txt
72 / 140 / 96 / 180
```

要有：

- 不完全對稱
- 空氣感
- 不規則節奏

---

# 四、Texture（時間感）

## 目前問題

畫面太乾淨。

像：

- Figma mockup
- 新開張 SaaS
- 無時間痕跡

侘寂核心之一：

> 時間感。

---

# 建議加入超淡 Texture

## 可加入：

- 和紙紋理
- 墨暈
- 紙纖維
- 水泥顆粒
- 毛邊暈染

透明度：

```css
opacity: 0.02 ~ 0.05;
```

重點：

第一眼不要看見。

而是：

第二眼才感受到。

---

# Texture 原則

不要：

- 明顯噪點
- 大理石紋
- 浮誇水墨背景
- 高對比貼圖

侘寂 texture 必須：

- 克制
- 幾乎不可察覺
- 像歲月留下的痕跡

---

# 五、Card 設計建議

## 目前問題

服務卡片偏：

- SaaS
- dashboard
- UI kit

缺少侘寂感。

---

# 建議

## 卡片風格

```css
background: var(--bg-secondary);
border: 1px solid var(--border);
border-radius: 20px;
box-shadow: none;
```

---

## Hover 效果

避免：

- 放大
- 發亮
- 漂浮陰影

改為：

```css
transition: all 0.25s ease;

:hover {
  background: #e5ddd2;
  border-color: #c8beb1;
}
```

---

# Icon 建議

目前 icon 太 UI kit。

建議：

- 線條更細
- 更像印章符號
- 更東方極簡

避免：

- Material icon 感
- 科技感 icon

---

# 六、Motion（非常重要）

## 目前缺少動態情緒

這是作品從：

「學生網站」

進化到：

「作品集等級」

最關鍵的地方。

---

# 建議動畫方向

## 原則

侘寂動畫：

不是炫技。

而是：

> 像空氣流動。

---

# 建議動畫

## 1. Hero Logo 呼吸感

墨暈：

超慢 scale + opacity。

時間：

```css
8s ~ 12s ease-in-out infinite
```

感覺像：

墨在紙上慢慢擴散。

---

## 2. Scroll Reveal

不要：

- 飛入
- 彈跳
- rotate

改為：

```css
opacity: 0 → 1
translateY: 12px → 0
```

速度慢。

像霧浮現。

---

## 3. Button Hover

避免：

- 強 hover
- Neon
- 發亮

改：

```css
background: var(--primary-dark);
transition: 0.2s ease;
```

即可。

---

# 七、金融感強化

## 目前偏：

- 茶室
- 香氛品牌
- 高級生活選物

金融秩序感略不足。

---

# 建議增加

## 精準細線

例如：

```css
border-top: 1px solid rgba(0,0,0,0.05);
```

讓畫面更有：

- 紀律
- 信任感
- 專業感

---

# Grid 對齊

侘寂不是亂。

而是：

> 克制後的秩序。

所有：

- 卡片
- icon
- title
- spacing

必須精準對齊。

---

# 八、整體設計哲學（建議寫進報告）

## 核心概念

```txt
金融不應製造焦慮，
而應像流水般穩定存在。
```

---

# 侘寂金融概念

本專題嘗試將：

- 東方侘寂美學
- 金融穩定感
- Calm Technology

融合於數位銀行介面。

透過：

- 留白
- 克制色彩
- 緩慢動態
- 非侵略式 UI

降低金融產品常見的：

- 壓迫感
- 焦慮感
- 過度科技化

使金融服務更接近：

- 安定
- 平衡
- 長期陪伴

---

# 九、絕對避免事項

## 禁止加入

### 視覺

- 科技藍
- 霓虹光
- 強陰影
- 漸層濫用
- 毛玻璃
- 過度動畫
- cyberpunk 感

---

### 動畫

- bounce
- zoom
- rotate
- flashy transition

---

### UI

- Material Design 感
- SaaS dashboard 感
- AI startup 感

---

# 十、最終目標

此專案應呈現：

```txt
不是「科技銀行」

而是：

一間有溫度、
有歲月感、
安靜而可信任的數位銀行。
```

---

# 最終方向總結

關鍵字：

- Calm
- Silence
- Balance
- Wabi-Sabi
- Trust
- Breathing Space
- Texture
- Restraint
- Timeless

---

# 核心設計原則（一句話）

```txt
侘寂不是把畫面做空。

而是讓每一個元素，
都像被時間溫柔留下來。
```