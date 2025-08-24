cl{{- define "front.name" -}}
{{ .Chart.Name }}
{{- end }}

{{- define "front.fullname" -}}
{{ .Release.Name }}-{{ .Chart.Name }}
{{- end }}

{{- define "front.selectorLabels" -}}
app: {{ include "blocker-service.name" . }}
{{- end }}

{{- define "front.labels" -}}
app: {{ include "front.name" . }}
chart: {{ .Chart.Name }}-{{ .Chart.Version }}
release: {{ .Release.Name }}
heritage: {{ .Release.Service }}
{{- end }}