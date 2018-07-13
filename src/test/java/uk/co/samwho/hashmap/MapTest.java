package uk.co.samwho.hashmap;

import com.google.common.collect.Iterables;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class MapTest {
    private final Map<String, String> underTest = new HashMap<>();

    @Test
    public void putReturnsPreviousValue() {
        assertThat(underTest.put("foo", "bar")).isNull();
        assertThat(underTest.put("foo", "baz")).isEqualTo("baz");
    }

    @Test
    public void getActuallyWorks() {
        assertThat(underTest.get("foo")).isNull();
        assertThat(underTest.put("foo", "bar")).isNull();
        assertThat(underTest.get("foo")).isEqualTo("bar");
    }

    @Test
    public void removeActuallyWorks() {
        assertThat(underTest.put("foo", "bar")).isNull();
        assertThat(underTest.get("foo")).isEqualTo("bar");
        assertThat(underTest.remove("foo")).isEqualTo("bar");
        assertThat(underTest.get("foo")).isNull();
    }

    @Test
    public void containsKeyActuallyWorks() {
        assertThat(underTest.containsKey("foo")).isFalse();
        assertThat(underTest.put("foo", "bar")).isNull();
        assertThat(underTest.containsKey("foo")).isTrue();
    }

    @Test
    public void containsValueActuallyWorks() {
        assertThat(underTest.containsValue("bar")).isFalse();
        assertThat(underTest.put("foo", "bar")).isNull();
        assertThat(underTest.containsValue("bar")).isTrue();
    }

    @Test
    public void keySetActuallyWorks() {
        assertThat(underTest.keySet()).isEmpty();
        assertThat(underTest.put("foo", "bar")).isNull();
        assertThat(underTest.keySet()).containsExactly("foo");
    }

    @Test
    public void valuesActuallyWorks() {
        assertThat(underTest.values()).isEmpty();
        assertThat(underTest.put("foo", "bar")).isNull();
        assertThat(underTest.values()).containsExactly("bar");
    }

    @Test
    public void entrySetActuallyWorks() {
        assertThat(underTest.entrySet()).isEmpty();
        assertThat(underTest.put("foo", "bar")).isNull();
        assertThat(underTest.entrySet()).hasSize(1);

        Map.Entry<String, String> entry = Iterables.getOnlyElement(underTest.entrySet());
        assertThat(entry.getKey()).isEqualTo("foo");
        assertThat(entry.getValue()).isEqualTo("bar");
    }

    @Test
    public void clearActuallyWorks() {
        assertThat(underTest).isEmpty();
        assertThat(underTest.put("foo", "bar")).isNull();
        assertThat(underTest).isNotEmpty();
        underTest.clear();
        assertThat(underTest).isEmpty();
    }

    @Test
    public void sizeActuallyWorks() {
        assertThat(underTest).isEmpty();
        assertThat(underTest.put("foo", "bar")).isNull();
        assertThat(underTest.put("bar", "baz")).isNull();
        assertThat(underTest).hasSize(2);
    }

    @Test
    public void resizingWorks() {
        HashMap<String, String> underTest = new HashMap<>(1, 1, 2);
        assertThat(underTest).isEmpty();
        assertThat(underTest.put("foo", "bar")).isNull();
        assertThat(underTest.put("bar", "baz")).isNull();
        assertThat(underTest).hasSize(2);
        assertThat(underTest.get("foo")).isEqualTo("bar");
        assertThat(underTest.get("bar")).isEqualTo("baz");
    }
}
