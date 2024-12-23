# Content Based Image Retrieval System

> ## Table of Contents
>
> - [**Getting Started**](#getting-started)
>   - [Prerequisites](#prerequisites)
>   - [Building \& Running](#building--running)
> - [**Background: Histogram Analysis \& Comparison**](#background-histogram-analysis-methods)
>   - [Intensity Method](#intensity-method)
>   - [Color-Code Method](#color-code-method)
>   - [Histogram Comparison](#histogram-comparison)
> - [**Background: Relevance Feedback**](#background-relevance-feedback)
>   - [Normalization](#normalization)
>   - [Distance Metric: Modified L1](#distance-metric-modified-l1)
>   - [Weight Calculation](#weight-calculation)
>

## Description

This repo contains my implementation of a simple Content-Based Image Retrieval
(CBIR) system. The project centered on the utilization of color histogram
comparison techniques, with a specific emphasis on the intensity and color-code
methods. These techniques involve the transformation of color values into
histograms, which serve as the basis for image comparison. The system also
incorporates a simplified relevance feedback mechanism to enhance retrieval
results.

## Getting Started

### Prerequisites

Before you begin, ensure you have the following tools installed on your system:

- [`maven`](https://maven.apache.org/)
- [`jdk 23`](https://openjdk.org/)

### Building & Running

Follow the steps below to compile and run the program:

1. Clone repository:

    ```bash
    git clone https://github.com/michael-re/content-retrieval.git
    ```

2. Navigate to project directory:

    ```bash
    cd content-retrieval/
    ```

3. Build:

    ```bash
    # build
    mvn clean package

    # clean up build artifacts
    mvn clean
    ```

4. Run:

    ```bash
    java -jar target/cbir.jar
    ```

---

## Background: Histogram Analysis Methods

### Intensity Method

The intensity method transforms a 24-bit rgb color (8 bits per channel) into a
single intensity value using the following formula:

## $`Intensity = (r \times 0.299) + (g \times 0.587) + (b \times 0.114)`$

$\text{Where:}$

- $r = \text{the red color channel of the pixel.}$
- $g = \text{the green color channel of the pixel.}$
- $b = \text{the blue color channel of the pixel.}$

By transforming the 24-bit rgb color into a single 8-bit value, 25 histogram
bins can be defined as follows:

- `H01: I ∈ [000,010)`
- `H02: I ∈ [010,020)`
- `H03: I ∈ [020,030)`
- `...`
- `H25: I ∈ [240,255)`

---

### Color-Code Method

The color-code method transforms the 24-bit rgb color intensities into a 6-bit
color-code. It accomplishes this by extracting the two most significant bits
from each color channel and combining them into a single 6-bit value
(as illustrated in the figure below).

![figure i: color-code illustration](docs/fig_colorcode.png)

By transforming the 24-bit rgb color into a single 6-bit value, 64 histogram
bins can be defined as follows:

- `H01: 000000`
- `H02: 000001`
- `H03: 000010`
- `...`
- `H64: 111111`

The table below shows how to compute the color-code value for the rgb color
`(128, 0, 255)` by combining the two most significant bits of each channel.

| Channel | Value | Channel Bitmap | Most Significant 2 bits |
|---------|-------|----------------|-------------------------|
| r       | `128` | `10000000`     | `10` → `0b100000`       |
| g       | `000` | `00000000`     | `00` → `0b000000`       |
| b       | `255` | `11111111`     | `11` → `0b000011`       |

Hence, the 6-bit color-code value for `(128, 0, 255)` is `0b100011`

---

## Histogram Comparison

To determine how similar two images are, the distance metrics for histogram
comparison is used. The difference between two images, denoted as $i$ and $k$,
is calculated using the Manhattan distance formula, taking into consideration
the pixel count in each image. The calculation is as follows:

### Manhattan Distance: L1

## $`D_{i,k} = \sum_j | \frac{H_i(j)}{M_i \times N_i} - \frac{H_k(j)}{M_k \times N_k} |`$

$\text{Where:}$

- $`j = \text{the index of the bin for } H_i \text{ and } H_k \text{.}`$
- $`H_i = \text{the histogram for the i-th image.}`$
- $`H_j = \text{the histogram for the k-th image.}`$
- $`M_i \times N_i = \text{the number of pixels in image } i \text{.}`$
- $`M_k \times N_k = \text{the number of pixels in image } k \text{.}`$

---

## Background: Relevance Feedback

The Relevance Feedback mechanism enhances the retrieval process by incorporating
user feedback and iteratively adjusting the feature weights based on that input.
This project employs a simplified relevance feedback mechanism, comprising five
phases:

1. Combine the intensity and color-code histograms for each image and normalize
   the feature matrix.
2. Gather user feedback in the form of relevant and non-relevant images.
3. Compute new weights using the feature (sub) matrix of the images marked relevant.
4. Retrieval - apply the weights to each feature using values from step `3` to
   compute the distance matrix.
5. Subsequent RF iterations:
    - Obtain feedback from the user.
    - Update feature weights.
    - Repeat retrieval.

### Normalization

In the initial step of the RF process, the intensity and color-code histogram
values are combined. Here, the bin values for each image are normalized by
dividing the bin value by the image's size. Then, the entire feature matrix is
normalized by applying the following formula:

## $`V_k = \left( \frac{V_k - \mu_k}{\sigma_k} \right)`$

$\text{Where:}$

- $\mu_k = \text{average value of feature         } k.$
- $\sigma_k = \text{standard deviation of feature } k.$

> [!NOTE]
> given that a subset of images are used during the RF process, the sample
> standard deviation is employed when computing $\sigma_k$.
>
> ## $`\sigma_k = \sqrt{ \frac{ \sum \left(X_i - avg \right)^2 }{N - 1}}`$

### Distance Metric: Modified L1

When calculating the distance between two images using RF analysis, a modified
version of the Manhattan Distance formula is used:

## $`D(I, J) = \sum_i \omega_i \times | V_i(I) - V_i(J) |`$

This version of the Manhattan distance incorporates the weight assigned to each
bin in the feature matrix. Moreover, the bins of the histogram are not
normalized by dividing them by the image size, as the feature matrix has
already been normalized in step [`1 - normalization`](#normalization).

### Weight Calculation

For the initial query results, no bias is applied to the weights as they are all
assigned the same value. To calculate this value, we use the following formula:

**Initial weight value:**

## $`W_i = \left( \frac{1}{\sigma_i} \right)`$

For subsequent queries where the user has provided feedback regarding image
relevance. A normalized weight value is used for each feature.

**Normalized weight value:**

## $`W_i = \left( \frac{W_i}{\sum W_i} \right)`$

> [!NOTE]
> in cases where the standard deviation $`\sigma_i`$ for feature $`i`$ of all
> the relevant marked images is $`0`$, the following steps are taken when
> updating weight $`W_i`$ (where $`m_i`$ is the mean of feature $`i`$):
>
> - if $`m_i = 0`$, set $`W_i = 0`$
> - if $`m_i \neq 0`$, set $`st_i`$ = $`\frac{min \{ \sigma, \sigma \neq0 \} }{2}`$,
>   and calculate $`W_i`$ as usual.
