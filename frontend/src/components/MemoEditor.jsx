import { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import memoService from '../services/memoService';
import './MemoEditor.css';

/**
 * MemoEditor 컴포넌트
 * 관심기업에 대한 메모를 작성하고 저장하는 에디터
 */
function MemoEditor({ stockCode, onSaveSuccess, onSaveError }) {
  const [content, setContent] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [lastSavedAt, setLastSavedAt] = useState(null);

  const MAX_LENGTH = 2000;

  // 메모 불러오기
  useEffect(() => {
    if (!stockCode) return;

    loadMemo();
  }, [stockCode]);

  // 메모 데이터 로딩 함수
  const loadMemo = async () => {
    setIsLoading(true);
    try {
      const data = await memoService.getMemo(stockCode);
      setContent(data.content || '');
      setLastSavedAt(data.updatedAt);
    } catch (error) {
      console.error('메모 로딩 실패:', error);
    } finally {
      setIsLoading(false);
    }
  };

  // 텍스트 변경 핸들러
  const handleChange = (e) => {
    const newContent = e.target.value;

    // 최대 글자 수 제한
    if (newContent.length <= MAX_LENGTH) {
      setContent(newContent);
    }
  };

  // 저장 핸들러
  const handleSave = async () => {
    setIsSaving(true);
    try {
      const result = await memoService.saveMemo(stockCode, content);
      setLastSavedAt(result.updatedAt);

      // 성공 콜백
      if (onSaveSuccess) {
        onSaveSuccess(result.message || '메모가 성공적으로 저장되었습니다');
      }
    } catch (error) {
      console.error('메모 저장 실패:', error);

      // 실패 콜백
      if (onSaveError) {
        onSaveError(error.message || '메모 저장에 실패했습니다');
      }
    } finally {
      setIsSaving(false);
    }
  };

  // 마지막 저장 시간 포맷팅
  const formatLastSavedTime = (dateString) => {
    if (!dateString) return null;

    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}.${month}.${day} ${hours}:${minutes}`;
  };

  if (isLoading) {
    return (
      <div className="memo-editor">
        <div className="memo-editor__loading">
          <div className="spinner"></div>
          <p>메모를 불러오는 중...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="memo-editor">
      {/* 헤더 */}
      <div className="memo-editor__header">
        <h3 className="memo-editor__title">메모</h3>
        {lastSavedAt && (
          <p className="memo-editor__last-saved">
            마지막 저장: {formatLastSavedTime(lastSavedAt)}
          </p>
        )}
      </div>

      {/* 텍스트 에디터 */}
      <div className="memo-editor__body">
        <textarea
          className="memo-editor__textarea"
          value={content}
          onChange={handleChange}
          placeholder="이 기업에 대한 메모를 작성해주세요..."
          maxLength={MAX_LENGTH}
          disabled={isSaving}
        />
      </div>

      {/* 푸터 */}
      <div className="memo-editor__footer">
        <div className="memo-editor__char-count">
          <span className={content.length >= MAX_LENGTH ? 'limit-reached' : ''}>
            {content.length}
          </span>
          <span className="char-count-separator">/</span>
          <span>{MAX_LENGTH}</span>
        </div>

        <button
          className="memo-editor__save-button"
          onClick={handleSave}
          disabled={isSaving}
        >
          {isSaving ? (
            <>
              <span className="button-spinner"></span>
              저장 중...
            </>
          ) : (
            '저장'
          )}
        </button>
      </div>
    </div>
  );
}

MemoEditor.propTypes = {
  stockCode: PropTypes.string.isRequired,
  onSaveSuccess: PropTypes.func,
  onSaveError: PropTypes.func,
};

export default MemoEditor;
